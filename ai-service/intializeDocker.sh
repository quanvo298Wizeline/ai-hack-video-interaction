docker pull ollama/ollama:lastest
docker run -d -v ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama

docker pull pgvector/pgvector:0.7.2-pg16

