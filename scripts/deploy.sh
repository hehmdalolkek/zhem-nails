#!/usr/bin/env bash

ssh hehmdalolkek@178.208.94.26 << EOF

cd app/zhem-nails
docker compose down
docker rmi zhem-nails-backend
docker rmi zhem-nails-client

git pull
cd zhem-service
mvn clean package
cd ../../zhem-nails/zhem-client
mvn clean package

docker compose up -d

EOF