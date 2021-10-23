ARG JAR_LOCATION

FROM openjdk:11.0.6-jre-buster
ARG JAR_LOCATION

RUN apt-get update && apt-get install -y \
  curl \
  && rm -rf /var/lib/apt/lists/*

COPY JAR_LOCATION /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
