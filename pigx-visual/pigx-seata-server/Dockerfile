FROM pig4cloud/java:8-jre

MAINTAINER wangiegie@gmail.com

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /pigx-seata-server

WORKDIR /pigx-seata-server

EXPOSE 8091

ADD ./target/pigx-seata-server.jar ./

CMD sleep 180;java $JAVA_OPTS -jar pigx-seata-server.jar
