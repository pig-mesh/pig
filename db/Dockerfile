FROM mysql:5.7

MAINTAINER lengleng(wangiegie@gmail.com)

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

COPY ./db/pig.sql /docker-entrypoint-initdb.d
