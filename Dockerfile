FROM java:8-alpine

ADD ./gehc-ai-datacatalog-app/target/*-exec.jar app.jar

EXPOSE 8282

ENTRYPOINT ["java","-Dserver.port=8282","-Dspring.profiles.active=aws","-Djava.security.egd=file:/dev/.urandom","-jar","/app.jar"]
