# Estágio 1: Build - Usa a imagem oficial do Maven com JDK 17 (Compatível com Quarkus)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

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
# CORREÇÃO CRUCIAL (LINHA 20): Copia o JAR com o nome 'biblioteca' e renomeia para 'teleconsulta.jar'
COPY --from=build /app/target/biblioteca-1.0.0-SNAPSHOT.jar teleconsulta.jar
# Define o comando de inicialização
CMD ["java", "-jar", "teleconsulta.jar"]