# Test plugin in docker linux container
# docker build -t foo . && docker run foo
FROM adoptopenjdk/openjdk11:alpine-slim

RUN java -version

ADD . /opt/test-in-docker

WORKDIR /opt/test-in-docker/

ENTRYPOINT ["./gradlew", "clean", "build"]