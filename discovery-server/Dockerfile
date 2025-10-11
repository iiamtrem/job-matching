# ---------- Build ----------
FROM maven:3.9-eclipse-temurin-17 AS build
# đặt cấu trúc giống repo thật: parent ở /src/pom.xml, module ở /src/discovery-server
WORKDIR /src/discovery-server
COPY pom.xml /src/pom.xml
COPY discovery-server/pom.xml ./pom.xml

# tải dependency chỉ cho module này (không đụng root aggregator)
RUN mvn -q -f pom.xml -DskipTests dependency:go-offline

# copy source của module
COPY discovery-server/. .

# build jar
RUN mvn -q -f pom.xml -DskipTests package

# ---------- Run ----------
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /src/discovery-server/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75","-jar","/app/app.jar","--spring.profiles.active=docker"]
