package com.wizeline.ai.hack.video.domain.repository.impl;

import com.wizeline.ai.hack.video.domain.data.MetaData;
import com.wizeline.ai.hack.video.domain.repository.DocumentVectorRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultDocumentVectorRepository implements DocumentVectorRepository {
  private final VectorStore vectorStore;

  public DefaultDocumentVectorRepository(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
    this.vectorStore = new PgVectorStore(jdbcTemplate, embeddingModel);
  }

  @Override
  public void add(List<Document> documents) {
    this.vectorStore.add(documents);
  }

  @Override
  public List<Document> retrieve(
      final String query, final MetaData.DataType dataType, final int size) {
    return this.vectorStore.similaritySearch(
        SearchRequest.query(query)
            .withFilterExpression(
                new Filter.Expression(
                    Filter.ExpressionType.EQ,
                    new Filter.Key(MetaData.DATATYPE),
                    new Filter.Value(dataType.toString())))
            .withTopK(size));
  }

  @Override
  public List<Document> retrieve(
      final String query,
      final List<UUID> uuids,
      final MetaData.DataType dataType,
      final int size) {

    var expression = getExpression(uuids);

    return this.vectorStore.similaritySearch(
        SearchRequest.query(query)
            .withFilterExpression(
                new Filter.Expression(
                    Filter.ExpressionType.AND,
                    new Filter.Expression(
                        Filter.ExpressionType.EQ,
                        new Filter.Key(MetaData.DATATYPE),
                        new Filter.Value(dataType.toString())),
                    new Filter.Group(expression)))
            .withTopK(size));
  }

  private Filter.Expression getExpression(final List<UUID> uuids) {
    Filter.Expression expression =
        new Filter.Expression(
            Filter.ExpressionType.EQ,
            new Filter.Key(MetaData.ID),
            new Filter.Value(uuids.get(0).toString()));

    for (int index = 1; index < uuids.size(); index++) {
      UUID nextUUID = uuids.get(index);
      expression =
          new Filter.Expression(
              Filter.ExpressionType.OR,
              expression,
              new Filter.Expression(
                  Filter.ExpressionType.EQ,
                  new Filter.Key(MetaData.ID),
                  new Filter.Value(nextUUID.toString())));
    }
    return expression;
  }
}
