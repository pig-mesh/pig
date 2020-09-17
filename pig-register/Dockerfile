FROM moxm/java:1.8-full

MAINTAINER wangiegie@gmail.com

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /pig-register

WORKDIR /pig-register

EXPOSE 8848

ADD ./target/pig-register.jar ./

CMD sleep 30;java $JAVA_OPTS -jar pig-register.jar


