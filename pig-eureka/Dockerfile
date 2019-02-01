FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /pig-eureka

WORKDIR /pig-eureka

EXPOSE 8761

ADD ./pig-eureka/target/pig-eureka.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar pig-eureka.jar
