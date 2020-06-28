# Test plugin in docker linux container
# docker build -t foo . && docker run -it foo
FROM adoptopenjdk/openjdk11:alpine-slim

RUN java -version

ADD . /opt/test-in-docker

WORKDIR /opt/test-in-docker/
RUN ./gradlew clean build