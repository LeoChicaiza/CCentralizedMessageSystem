# Etapa 1: Compilar el proyecto
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

# Etapa 2: Crear imagen con Tomcat
FROM eclipse-temurin:21-jdk
WORKDIR /usr/local/tomcat

# Descargar Tomcat (ajusta la versión según la última disponible)
RUN apt-get update && apt-get install -y wget unzip && \
    wget https://downloads.apache.org/tomcat/tomcat-10/v10.1.41/bin/apache-tomcat-10.1.41.tar.gz && \
    tar xzf apache-tomcat-10.1.41.tar.gz && \
    mv apache-tomcat-10.1.41 tomcat && \
    rm apache-tomcat-10.1.41.tar.gz

# Copiar el .war generado
COPY --from=build /app/target/*.war /usr/local/tomcat/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["bash", "tomcat/bin/catalina.sh", "run"]
