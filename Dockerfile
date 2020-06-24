# docker build -t foo . && docker run --rm -it foo
FROM adoptopenjdk/openjdk11:alpine-jre-nightly

RUN java -version

ADD . /opt/test-in-docker

WORKDIR /opt/test-in-docker/
RUN ls -al
RUN ./gradlew
ENTRYPOINT ["./gradlew", ":examples:sample-python-project:runNumpy"]