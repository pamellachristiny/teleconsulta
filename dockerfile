# Estágio 1: Build (CORRIGIDO: Tag para Maven 3 com JDK 17)
FROM maven:3-jdk-17 AS build
WORKDIR /app
# Copia o pom.xml para instalar dependências
COPY pom.xml .
RUN mvn dependency:go-offline
# Copia todo o código fonte
COPY src ./src
# Executa a compilação final, criando o JAR
RUN mvn clean install -DskipTests
# Estágio 2: Run (CORRIGIDO: Tag JRE leve para openjdk 17)
FROM openjdk:17-slim
WORKDIR /app
# Copia o JAR do estágio de build
COPY --from=build /app/target/teleconsulta-1.0-SNAPSHOT.jar teleconsulta.jar
# Define o comando de inicialização
CMD ["java", "-jar", "teleconsulta.jar"]