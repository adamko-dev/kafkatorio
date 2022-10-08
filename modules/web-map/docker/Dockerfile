# syntax=docker/dockerfile:1

FROM nginx

EXPOSE 80

COPY ./docker/nginx.conf /etc/nginx/nginx.conf

COPY ./build/distributions /usr/share/nginx/html
