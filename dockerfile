# Estágio 1: Build (Usando uma tag Maven 3.9.5 e OpenJDK 17 slim)
FROM maven:3.9.5-openjdk-17-slim AS build
WORKDIR /app
# Copia o pom.xml para instalar dependências
COPY pom.xml .
RUN mvn dependency:go-offline
# Copia todo o código fonte
COPY src ./src
# Executa a compilação final, criando o JAR
RUN mvn clean install -DskipTests

# Estágio 2: Run (Usando Eclipse Temurin, que é leve e estável para JRE 17)
FROM eclipse-temurin:17-jre-focal
WORKDIR /app
# Copia o JAR do estágio de build
COPY --from=build /app/target/teleconsulta-1.0-SNAPSHOT.jar teleconsulta.jar
# Define o comando de inicialização
CMD ["java", "-jar", "teleconsulta.jar"]