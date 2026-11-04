# --- Build ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q clean package

# --- Laufzeit: Tomcat 10.1 (Jakarta EE 10) ---
FROM tomcat:10.1-jdk17-temurin
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /build/target/Fallstudie_IU.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
