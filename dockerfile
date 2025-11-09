# Etapa 1: Build da aplicação
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copia o código-fonte e o arquivo pom.xml
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

# Compila e empacota o projeto
RUN mvn package -DskipTests

# Etapa 2: Execução da aplicação
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia o JAR gerado no estágio de build
COPY --from=build /app/target/biblioteca-1.0.0-SNAPSHOT.jar biblioteca.jar

# Expõe a porta padrão do Quarkus
EXPOSE 8080

# Comando para iniciar a aplicação
CMD ["java", "-jar", "biblioteca.jar"]
