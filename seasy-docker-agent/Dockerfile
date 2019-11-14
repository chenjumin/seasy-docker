FROM openjdk:8-jre
MAINTAINER chenjumin@163.com

ARG build_message
ARG SEASY_DOCKER_AGENT__SERVER_IP=${SEASY_DOCKER_AGENT__SERVER_IP}

WORKDIR /opt/seasy/docker-agent
COPY target/seasy-docker-agent-release/seasy-docker-agent .

RUN echo "$build_message" \
	&& "$SEASY_DOCKER_AGENT__SERVER_IP" \
	&& chmod 777 docker_run.sh

CMD ["sh", "docker_run.sh"]
