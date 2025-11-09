# Etapa 1 - Build da aplicação
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Copia o arquivo de configuração do Maven e baixa dependências (cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código-fonte da aplicação
COPY src ./src

# Compila o projeto e empacota o JAR
RUN mvn clean package -DskipTests

# Etapa 2 - Imagem final de execução
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copia o JAR gerado do estágio de build
COPY --from=build /app/target/biblioteca-1.0.0-SNAPSHOT.jar app.jar

# Define a porta exposta (Render usará esta)
EXPOSE 8080

# Comando para rodar o aplicativo
CMD ["java", "-jar", "app.jar"]
