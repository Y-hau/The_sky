FROM hub.c.163.com/library/java:8-alpine

ADD target/*.jar aurekeApp.jar

EXPOSE 8100

ENTRYPOINT ["java", "-jar", "/aurekeApp.jar"]