#!/bin/bash

echo "=========================="
echo "stopping container"
echo "----------------"

docker container stop rabbitmq-otus

echo "=========================="
echo "remove old image"
echo "----------------"

docker container rm rabbitmq-otus

echo "=========================="
echo "Creating container"
echo "----------------"

docker run --name rabbitmq-otus  \
             -e RABBITMQ_DEFAULT_USER=guest     \
             -e RABBITMQ_DEFAULT_PASS=guest    \
             -e RABBITMQ_DEFAULT_VHOST='/' -p 5672:5672 -p 15672:15672\
             -d rabbitmq:3.8-management


sleep 3
echo "=========================="
echo "launch gradle"
echo "----------------"
cd ..
./gradlew :ok-marketplace-app-rabbit:clean
./gradlew :ok-marketplace-app-rabbit:build

echo "=========================="
echo "local env was created"
echo "----------------"