# ---------- Build ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /src/user-service
COPY pom.xml /src/pom.xml
COPY user-service/pom.xml ./pom.xml
RUN mvn -q -f pom.xml -DskipTests dependency:go-offline
COPY user-service/. .
RUN mvn -q -f pom.xml -DskipTests package

# ---------- Run ----------
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /src/user-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75","-jar","/app/app.jar","--spring.profiles.active=docker"]
