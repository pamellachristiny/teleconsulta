# Etapa 1 - Build da aplicação
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Copia os arquivos do Maven e baixa dependências primeiro (cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código-fonte
COPY src ./src

# Compila o projeto e empacota o JAR
RUN mvn clean package -DskipTests

# Etapa 2 - Imagem final de execução
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copia o JAR do estágio de build
COPY --from=build /app/target/biblioteca-1.0.0-SNAPSHOT.jar app.jar

# Expõe a porta (ajuste se necessário)
EXPOSE 8080

# Comando de inicialização
CMD ["java", "-jar", "app.jar"]
