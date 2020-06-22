# pig4cloud/java:8镜像增加了中文字体与wait-for-it.sh的支持
# 镜像链接:https://hub.docker.com/r/pig4cloud/java/
# wait-for-it.sh采用https://github.com/vishnubob/wait-for-it作为解决方案
FROM pig4cloud/java:8

MAINTAINER wangiegie@gmail.com

ARG JAR_FILE=./target/pig-monitor.jar

# JVM调优参数等额外参数
ENV PARAMS ""

ENV NACOS_HOST pig-register

ENV NACOS_PORT 8848

ENV TZ=PRC

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /tmp

EXPOSE 5001

ADD ${JAR_FILE} ./app.jar

ENTRYPOINT ["wait-for-it.sh","$NACOS_HOST:$NACOS_PORT","--","java","-jar","app.jar","-Djava.security.egd=file:/dev/./urandom","$PARAMS"]
