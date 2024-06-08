FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/java:21-anolis

MAINTAINER wangiegie@gmail.com

ENV TZ=Asia/Shanghai
ENV LANG C.UTF-8
ENV JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /pigx-daemon-elastic-job

WORKDIR /pigx-daemon-elastic-job

EXPOSE 5002

ADD ./target/pigx-daemon-elastic-job.jar ./

CMD sleep 120;java $JAVA_OPTS -jar pigx-daemon-elastic-job.jar
