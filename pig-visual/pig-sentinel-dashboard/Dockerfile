FROM moxm/java:1.8-full

MAINTAINER wangiegie@gmail.com

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /pig-sentinel-dashboard

WORKDIR /pig-sentinel-dashboard

EXPOSE 5003

ADD ./target/pig-sentinel-dashboard.jar ./

CMD sleep 60;java $JAVA_OPTS -jar pig-sentinel-dashboard.jar
