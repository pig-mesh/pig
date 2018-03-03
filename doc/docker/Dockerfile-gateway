FROM java:8-jre
MAINTAINER Pig lengleng <wangiegie@gmail.com>

ADD ./jar/pig-gateway.jar /app/
ADD ./jar/pinpoint-agent /app/pinpoint-agent

CMD ["java", "-Xmx500m", "-javaagent:/app/pinpoint-agent/pinpoint-bootstrap-1.5.0.jar", "-Dpinpoint.agentId=pig-gateway", "-Dpinpoint.applicationName=GATEWAY", "-jar", "/app/pig-gateway.jar"]

EXPOSE 9999