FROM openjdk:8-jre
MAINTAINER chenjumin@163.com

ARG build_message

WORKDIR /opt/seasy/docker-agent
COPY target/seasy-docker-agent-release/seasy-docker-agent .

RUN echo "$build_message" \
	&& chmod 777 docker_run.sh

CMD ["sh", "docker_run.sh"]
