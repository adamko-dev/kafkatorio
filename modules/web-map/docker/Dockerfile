# syntax=docker/dockerfile:1

FROM nginx

COPY ./docker/nginx.conf /etc/nginx/nginx.conf

COPY ./build/distributions /usr/share/nginx/html
