# Estágio 1: Build - Usamos uma tag oficial do Maven com JDK 17
# Essa tag garante que o Maven seja compatível com o Quarkus
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Você pode remover a linha "RUN apt-get update && apt-get install -y maven..."

# Copia o pom.xml para instalar dependências
COPY pom.xml .
RUN mvn dependency:go-offline
# Copia todo o código fonte
COPY src ./src
# Executa a compilação final, criando o JAR
RUN mvn clean install -DskipTests

# Estágio 2: Run - Usa Eclipse Temurin JRE 17 (muito mais leve para rodar)
FROM eclipse-temurin:17-jre-focal
WORKDIR /app
# Copia o JAR do estágio de build
COPY --from=build /app/target/teleconsulta-1.0-SNAPSHOT.jar teleconsulta.jar
# Define o comando de inicialização
CMD ["java", "-jar", "teleconsulta.jar"]