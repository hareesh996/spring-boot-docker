# syntax=docker/dockerfile:1

WORKDIR /home/gradle/project

EXPOSE 8082

## RUN apk update

ENV GRADLE_USER_HOME /home/gradle/project

COPY . /home/gradle/project

RUN gradle :shopping-cart-api:build