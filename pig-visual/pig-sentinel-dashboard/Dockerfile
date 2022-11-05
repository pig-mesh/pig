FROM moxm/java:1.8-full

RUN mkdir -p /pig-sentinel-dashboard

WORKDIR /pig-sentinel-dashboard

ARG JAR_FILE=target/pig-sentinel-dashboard.jar

COPY ${JAR_FILE} app.jar

EXPOSE 5003

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

CMD sleep 60; java $JAVA_OPTS -jar app.jar
