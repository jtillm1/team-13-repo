# build the Spring Boot application
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

# copy Maven files and source code
COPY . .

# build the JAR file
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# run the application
FROM eclipse-temurin:25-jre

WORKDIR /app

# copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# start Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]