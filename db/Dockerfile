FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/mysql-server:8.0.32

MAINTAINER lengleng(wangiegie@gmail.com)

ENV TZ=Asia/Shanghai
ENV LANG C.UTF-8

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./1schema.sql /docker-entrypoint-initdb.d

COPY ./2pigxx.sql /docker-entrypoint-initdb.d

COPY ./3pigxx_flow.sql /docker-entrypoint-initdb.d

COPY ./4pigxx_job.sql /docker-entrypoint-initdb.d

COPY ./5pigxx_mp.sql /docker-entrypoint-initdb.d

COPY ./6pigxx_config.sql /docker-entrypoint-initdb.d

COPY ./7pigxx_pay.sql /docker-entrypoint-initdb.d

COPY ./8pigxx_codegen.sql /docker-entrypoint-initdb.d

COPY ./99pigxx_bi.sql /docker-entrypoint-initdb.d

COPY ./999pigxx_app.sql /docker-entrypoint-initdb.d
