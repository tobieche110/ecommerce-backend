# Etapa 1: Construcción
FROM openjdk:17-jdk-slim AS build

WORKDIR /app

# Copia el pom.xml y el Maven Wrapper
COPY pom.xml .
COPY mvnw .
COPY .mvn ./.mvn
COPY src ./src

# Ejecuta Maven para construir la aplicación
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Etapa 2: Imagen final
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copia el archivo JAR generado
COPY --from=build /app/target/ecommerce-0.0.1-SNAPSHOT.jar ecommerce-api.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "ecommerce-api.jar"]
