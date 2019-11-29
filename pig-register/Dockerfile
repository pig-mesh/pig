FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /pig-register

WORKDIR /pig-register

EXPOSE 8848

ADD ./pig-register/target/pig-register.jar ./


CMD sleep 30;java -Djava.security.egd=file:/dev/./urandom -jar pig-register.jar


