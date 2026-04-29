FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/java:21-anolis

WORKDIR /pig-register

ARG JAR_FILE=target/pig-register.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8848 9848 8080

ENV TZ=Asia/Shanghai \
    NACOS_JVM_OPTS="--add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED" \
    JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

CMD sleep 30; java $NACOS_JVM_OPTS $JAVA_OPTS -jar app.jar
