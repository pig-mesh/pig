FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /pigx-upms

WORKDIR /pigx-upms

EXPOSE 4000

ADD ./pig-upms/pig-upms-biz/target/pig-upms-biz.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar pig-upms-biz.jar
