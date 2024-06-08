FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/java:21-anolis

MAINTAINER wangiegie@gmail.com

ENV TZ=Asia/Shanghai
ENV LANG C.UTF-8
ENV JAVA_OPTS="-Xms512m -Xmx1024m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /pigx-flow-task

WORKDIR /pigx-flow-task

EXPOSE 9030

ADD ./target/pigx-flow-task-biz.jar ./

CMD sleep 60;java $JAVA_OPTS -jar pigx-flow-task-biz.jar
