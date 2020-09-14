FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /pig-xxl-job

WORKDIR /pig-xxl-job

EXPOSE 5004

ADD ./target/pig-xxl-job.jar ./

CMD sleep 60;java -Djava.security.egd=file:/dev/./urandom -jar pig-xxl-job.jar
