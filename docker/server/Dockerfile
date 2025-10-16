FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY server/build/libs/server-all.jar ./server.jar
EXPOSE 5050 5051 8080
ENTRYPOINT ["java","-jar","/app/server.jar"]
