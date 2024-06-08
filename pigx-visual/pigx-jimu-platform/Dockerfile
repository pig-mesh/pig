FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/java:21-anolis

MAINTAINER wangiegie@gmail.com

ENV TZ=Asia/Shanghai
ENV LANG C.UTF-8
ENV JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /pigx-jimu-platform

WORKDIR /pigx-jimu-platform

EXPOSE 5008

ADD ./target/pigx-jimu-platform.jar ./

CMD sleep 120;java $JAVA_OPTS -jar pigx-jimu-platform.jar
