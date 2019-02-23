FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /pig-zipkin

WORKDIR /pig-zipkin

EXPOSE 5002

ADD ./pig-visual/pig-zipkin/target/pig-zipkin.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar pig-zipkin.jar
