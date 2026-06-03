FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/java:21-anolis

WORKDIR /pig-boot

# 安装中文字体（解决词云中文显示问题）
RUN yum install -y wqy-microhei-fonts && yum clean all

ARG JAR_FILE=target/pig-boot.jar

COPY ${JAR_FILE} app.jar

EXPOSE 9999

ENV TZ=Asia/Shanghai JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"

CMD sleep 15; java $JAVA_OPTS -jar app.jar
