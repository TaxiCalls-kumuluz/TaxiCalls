#!/bin/bash

if [ "$(whoami)" != 'root' ]; then
    echo "You have no permission to run $0 as non-root user."
    exit 1;
fi

if [ -z $1 ]; then
	mvn clean package $2
	docker-compose -f docker-compose.yml -f docker-compose.dev.yml up --build
else
	docker-compose -f docker-compose.yml -f docker-compose.dev.yml kill $1
	mvn clean package $2
	docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d --build $1
fi