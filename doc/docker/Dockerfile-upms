FROM java:8-jre
MAINTAINER Pig lengleng <wangiegie@gmail.com>

ADD ./jar/pig-upms-service.jar /app/
ADD ./jar/pinpoint-agent /app/pinpoint-agent

CMD ["java", "-Xmx1000m", "-javaagent:/app/pinpoint-agent/pinpoint-bootstrap-1.5.0.jar", "-Dpinpoint.agentId=pig-upms-service", "-Dpinpoint.applicationName=PIG-UPMS-SERVICE", "-jar", "/app/pig-upms-service.jar"]

