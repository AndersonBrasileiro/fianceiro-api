# Estágio 1: Compilação da aplicação usando Maven e Eclipse Temurin Java 17
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Execução da aplicação usando uma imagem leve e atualizada do Java 17
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]