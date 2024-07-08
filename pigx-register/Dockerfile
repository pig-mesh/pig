FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/java:21-anolis

RUN mkdir -p /pigx-register

WORKDIR /pigx-register

ARG JAR_FILE=target/pigx-register.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8848 9848

ENV TZ=Asia/Shanghai
ENV LANG C.UTF-8 JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

CMD sleep 30; java $JAVA_OPTS -jar app.jar
