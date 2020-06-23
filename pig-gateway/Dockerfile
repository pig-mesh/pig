FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /pig-gateway

WORKDIR /pig-gateway

EXPOSE 9999

ADD ./target/pig-gateway.jar ./

CMD sleep 60;java -Djava.security.egd=file:/dev/./urandom -jar pig-gateway.jar
