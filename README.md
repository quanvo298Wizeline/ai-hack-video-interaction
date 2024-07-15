# ai-hack-video-interaction

## AI-Service

The backend will provide a video endpoint to save videos, segments, and frames to the database. Additionally, the
backend resizes these to a format that the LLava model can process. The LLava model then generates a description of the
image based on the provided prompt. This image description is then used to create embeddings, which are stored with the
description in the PGVector database.

The backend will also provide functionality to query videos and segments based on a user-provided prompt. The backend
converts the question into embeddings and searches the PGVector database for the nearest entry. This entry includes the
row ID of the image table along with the image and metadata. The image table data is then queried, combined with the
description, and presented to the user.

- Java with Springboot framework. (Java 17 and Springboot 3)
- AI - SpringBoot
- Database - Postgre
- Vector database - PgVector
- LLM Model: Ollama - Llava to transform from image to text
- LLM Model / Embedding model: AWS

### Prepare

- Copy from application-ollama.yaml and rename to application-local.yaml
- Replace aivideo.aws.region, aivideo.aws.s3Bucket, aivideo.aws.accessKey and aivideo.aws.secretKey properties
- Setup Docker

1. Pull images
   ```bash
   ./intializeDocker.sh
   ```
2. Start Local Ollama
   ```bash
   ./runOllama.sh
   ```
3. Start Postgre
   ```bash
   ./runPostgreDocker.sh
   ```

- Prepare database - Connect to database and run below script

```bash
CREATE EXTENSION vector;

CREATE EXTENSION "uuid-ossp" SCHEMA public;

CREATE TABLE IF NOT EXISTS vector_store (
	id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
	content text,
	metadata json,
	embedding vector(384)
)

CREATE INDEX IF NOT EXISTS spring_ai_vector_index ON vector_store USING HNSW (embedding vector_cosine_ops)
   ```

### Command

1. Start local

   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
   ```

Or Jar file:

   ```bash
    java -jar your-application.jar --spring.profiles.active=local
   ```

2. Start app local and debug

   ```bash
    ./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Duser.timezone=UTC -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5115" -Dspring-boot.run.profiles=local
   ```
