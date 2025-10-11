# ---------- Build ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /src/job-service
COPY pom.xml /src/pom.xml
COPY job-service/pom.xml ./pom.xml
RUN mvn -q -f pom.xml -DskipTests dependency:go-offline
COPY job-service/. .
RUN mvn -q -f pom.xml -DskipTests package

# ---------- Run ----------
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /src/job-service/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75","-jar","/app/app.jar","--spring.profiles.active=docker"]
