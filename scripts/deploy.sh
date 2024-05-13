#!/usr/bin/env bash

ssh hehmdalolkek@89.111.173.149 << EOF

cd zhem-nails

sudo docker-compose down
sudo docker rmi zhem-nails-backend
sudo docker rmi zhem-nails-client

git pull

mvn clean package

sudo docker-compose up -d

EOF