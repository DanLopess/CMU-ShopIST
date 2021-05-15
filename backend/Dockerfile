FROM maven:3-jdk-11 as maven
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY ./src ./src
ARG PROFILE=test
EXPOSE 8999/tcp
RUN mvn -P$PROFILE -Dmaven.test.skip=true package

FROM azul/zulu-openjdk-alpine:11
COPY --from=maven target/server-0.0.1-SNAPSHOT.jar ./
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar", "./server-0.0.1-SNAPSHOT.jar"]