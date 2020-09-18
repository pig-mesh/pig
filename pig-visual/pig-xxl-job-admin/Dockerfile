FROM moxm/java:1.8-full

MAINTAINER wangiegie@gmail.com

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /pig-xxl-job-admin

WORKDIR /pig-xxl-job-admin

EXPOSE 5004

ADD ./target/pig-xxl-job-admin.jar ./

CMD sleep 60;java $JAVA_OPTS -jar pig-xxl-job-admin.jar
