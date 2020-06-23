FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /pig-sentinel

WORKDIR /pig-sentinel

EXPOSE 5003

ADD ./target/pig-sentinel-dashboard.jar ./

CMD sleep 30;java -Djava.security.egd=file:/dev/./urandom -jar pig-sentinel-dashboard.jar
