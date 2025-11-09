# Etapa 1: Build da aplicação
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

# Etapa 2: Execução
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia o runner JAR completo do Quarkus
COPY --from=build /app/target/quarkus-app /app

EXPOSE 8080

CMD ["java", "-jar", "/app/quarkus-run.jar"]

