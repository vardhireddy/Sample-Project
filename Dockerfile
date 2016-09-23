FROM java:8-alpine

ADD target/*.jar app.jar

EXPOSE 8282

ENTRYPOINT ["java","-Dserver.port=8282","-Dspring.profiles.active=aws","-Djava.security.egd=file:/dev/.urandom","-jar","/app.jar"]
