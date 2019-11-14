#!/bin/bash

cd /opt/projects/seasy-docker/seasy-docker-agent
git pull origin master

mvn clean assembly:assembly -Ddockerfile.tag.skip

docker rm -f seasy-docker-agent

docker run -d --rm --name seasy-docker-agent -v /var/run/docker.sock:/var/run/docker.sock 192.168.134.139/seasy/seasy-docker-agent:0.0.1

docker ps -a
