#!/usr/bin/env bash

ssh hehmdalolkek@89.111.173.149 << EOF

cd zhem-nails

docker-compose down
docker rmi zhem-nails-backend
docker rmi zhem-nails-client

git pull

mvn clean package

docker-compose up -d

EOF