# Usa uma imagem base Java com Maven para o build
FROM maven:3.8.6-jdk-17 AS build
WORKDIR /app
# Copia o pom.xml para instalar dependências
COPY pom.xml .
RUN mvn dependency:go-offline
# Copia todo o código fonte
COPY src ./src
# Executa a compilação final, criando o JAR
RUN mvn clean install -DskipTests

# Usa uma imagem base Java mais leve para rodar a aplicação
FROM eclipse-temurin:17-jre-focal
WORKDIR /app
# Copia o JAR do estágio de build
COPY --from=build /app/target/teleconsulta-1.0-SNAPSHOT.jar teleconsulta.jar

CMD ["java", "-jar", "teleconsulta.jar"]