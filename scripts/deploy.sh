#!/usr/bin/env bash

ssh hehmdalolkek@178.208.94.26 << EOF

docker compose down
docker rm zhem_postgres
docker rm zhem_service
docker rm zhem_client
docker rmi zhem-nails-backend
docker rmi zhem-nails-client

cd app/zhem-nails
git pull
cd zhem-service
mvn clean package
cd ../../zhem-nails/zhem-client
mvn clean package

docker compose up -d

EOF