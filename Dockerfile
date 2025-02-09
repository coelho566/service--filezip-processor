# Usando a imagem oficial do Maven para compilar a aplicação
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia os arquivos do projeto para dentro do container
COPY pom.xml .
COPY src ./src

# Faz o build da aplicação
RUN mvn clean package -DskipTests

# Usando a imagem oficial do OpenJDK 21 para rodar a aplicação
FROM eclipse-temurin:21-jdk

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o JAR gerado pelo estágio de build
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta da aplicação
EXPOSE 8080

ARG MONGO_URI
ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY
ARG AWS_SESSION_TOKEN

#
ENV MONGO_URI=${MONGO_URI}
ENV AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}
ENV AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}
ENV AWS_SESSION_TOKEN=${AWS_SESSION_TOKEN}



# Comando de execução da aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
