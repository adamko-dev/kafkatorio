# syntax=docker/dockerfile:1

ARG KAFKATORIO_VERSION

#FROM gradle:7.4.2-jdk11 as builder
FROM eclipse-temurin:11-jdk-jammy as builder

WORKDIR /app

ENV GRADLE_USER_HOME /gradle-home
ENV GRADLE_OPTS "-Dorg.gradle.daemon=false -Dorg.gradle.logging.stacktrace=full -Dorg.gradle.console=plain"

COPY . .
RUN ./gradlew assemble



FROM builder

RUN ./gradlew assemble
