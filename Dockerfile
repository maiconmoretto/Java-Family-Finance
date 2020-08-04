FROM openjdk:11-jre-slim

MAINTAINER Maicon Moretto <maiconmorettos@gmail.com>

WORKDIR /app

COPY target/familyfinance-0.0.1-SNAPSHOT.jar /app/

CMD java -jar familyfinance-0.0.1-SNAPSHOT.jar


