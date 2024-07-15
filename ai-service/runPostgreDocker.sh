#!/bin/sh
#docker pull ankane/pgvector:latest
#docker pull pgvector/pgvector:0.7.2-pg16
docker run --name ai-video-postgres-ollama-072 -e POSTGRES_PASSWORD=nimda@123 -e POSTGRES_USER=ai-hack -e POSTGRES_DB=aivideohack -v ~/.docker-volumes/aihack/dbdata:/var/lib/postgresql/data -p 5432:5432 -d pgvector/pgvector:0.7.2-pg16