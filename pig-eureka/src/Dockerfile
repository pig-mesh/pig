FROM java:8-jre
MAINTAINER Pig lengleng <wangiegie@gmail.com>

ADD ./target/pig-eureka.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/pig-eureka.jar"]

EXPOSE 1025