#Usando a imagem oficial do Maven para compilar o projeto Java
FROM maven:3.8.4-openjdk-17-slim AS build

# Definindo o diretório de trabalho dentro do container
WORKDIR /app

# Copiando o arquivo POM e os arquivos fonte do projeto
COPY pom.xml .
COPY src ./src

# Compilando o projeto com Maven
RUN mvn clean install -DskipTests

#Usando uma imagem base JDK para executar a aplicação Java
FROM openjdk:17-jdk-slim

# Definindo o diretório de trabalho dentro do container
WORKDIR /app

# Copiando o JAR gerado para o diretório de trabalho
COPY --from=build /app/target/springblog-0.0.1-SNAPSHOT.jar ./app.jar

# Instalando wait-for-it.sh para poder esperar o rabbit e mongo
RUN apt-get update -y \
    && apt-get install -y wget netcat \
    && wget -O /usr/local/bin/wait-for-it.sh https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh \
    && chmod +x /usr/local/bin/wait-for-it.sh

# Aguardando até que o RabbitMQ e o MongoDB iniciem antes de iniciar a aplicação
CMD ["/bin/bash", "-c", "wait-for-it.sh -t 0 mongodb:27017 -- java -jar app.jar"]