FROM moxm/java:1.8-full as builder
WORKDIR /build
ARG JAR_FILE=target/pig-sentinel-dashboard.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract && rm app.jar

FROM moxm/java:1.8-full
LABEL maintainer="jclazz@outlook.com"
ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"
WORKDIR pig-sentinel-dashboard

COPY --from=builder /build/dependencies/ ./
COPY --from=builder /build/snapshot-dependencies/ ./
COPY --from=builder /build/spring-boot-loader/ ./
COPY --from=builder /build/application/ ./

EXPOSE 5003

CMD sleep 60; java $JAVA_OPTS org.springframework.boot.loader.JarLauncher