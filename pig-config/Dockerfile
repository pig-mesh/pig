FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /pig-config

WORKDIR /pig-config

EXPOSE 4001

ADD ./pig-config/target/pig-config.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar pig-config.jar
