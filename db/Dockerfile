FROM mysql:5.7

MAINTAINER lengleng(wangiegie@gmail.com)

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

COPY ./pig.sql /docker-entrypoint-initdb.d

COPY ./pig_config.sql /docker-entrypoint-initdb.d
