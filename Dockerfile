# docker build -t foo . && docker run -it foo
FROM adoptopenjdk/openjdk11:alpine-jre-nightly

RUN java -version

ADD . /opt/test-in-docker

WORKDIR /opt/test-in-docker/
RUN ls -al
RUN ./gradlew