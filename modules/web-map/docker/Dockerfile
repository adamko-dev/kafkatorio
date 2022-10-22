# syntax=docker/dockerfile:1

FROM nginx
ARG APP_FILES="build/"

EXPOSE 80

COPY ./nginx.conf /etc/nginx/nginx.conf

COPY $APP_FILES /usr/share/nginx/html
