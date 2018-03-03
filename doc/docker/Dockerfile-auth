FROM java:8-jre
MAINTAINER Pig lengleng <wangiegie@gmail.com>

ADD ./jar/pig-auth.jar /app/
ADD ./jar/pinpoint-agent /app/pinpoint-agent

CMD ["java", "-Xmx500m", "-javaagent:/app/pinpoint-agent/pinpoint-bootstrap-1.5.0.jar", "-Dpinpoint.agentId=pig-auth", "-Dpinpoint.applicationName=AUTH", "-jar", "/app/pig-auth.jar"]
