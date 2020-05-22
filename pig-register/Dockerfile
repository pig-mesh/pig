# pig4cloud/java:8-jre镜像增加了中文字体与wait-for-it.sh的支持
# 镜像链接:https://hub.docker.com/r/pig4cloud/java/
# wait-for-it.sh采用https://github.com/vishnubob/wait-for-it作为解决方案
FROM pig4cloud/java:8-jre

MAINTAINER wangiegie@gmail.com

ARG JAR_FILE=./target/pig-register.jar

# JVM调优参数等额外参数
ENV PARAMS ""

ENV MYSQL_HOST pig-mysql

ENV MYSQL_PORT 3306

ENV TZ=PRC

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /tmp

EXPOSE 8848

ADD ${JAR_FILE} ./app.jar

ENTRYPOINT ["wait-for-it.sh","$MYSQL_HOST:$MYSQL_PORT","--","java", "-jar","app.jar","-Djava.security.egd=file:/dev/./urandom","$PARAMS"]
