FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /pigx-upms

WORKDIR /pigx-upms

EXPOSE 4000

ADD ./pigx-upms/pigx-upms-biz/target/pigx-upms-biz.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar pigx-upms-biz.jar
