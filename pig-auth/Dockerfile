FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /pig-auth

WORKDIR /pig-auth

EXPOSE 3000

ADD ./target/pig-auth.jar ./

CMD sleep 60;java -Djava.security.egd=file:/dev/./urandom -jar pig-auth.jar
