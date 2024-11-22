FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/java:21-anolis

WORKDIR /pigx-register

ENV TZ=Asia/Shanghai \
    LANG=C.UTF-8 \
    JAVA_OPTS="-Xms512m -Xmx1024m -Djava.security.egd=file:/dev/./urandom"

ARG JAR_FILE=target/pigx-register.jar

COPY ${JAR_FILE} /pigx-register/app.jar

EXPOSE 8848 9848

CMD sleep 30; java $JAVA_OPTS -jar app.jar
