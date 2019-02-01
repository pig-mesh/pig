FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /pig-codegen

WORKDIR /pig-codegen

EXPOSE 5003

ADD ./pig-visual/pig-codegen/target/pig-codegen.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar pig-codegen.jar
