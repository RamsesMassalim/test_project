FROM openjdk:17-jdk

ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /usr/wait-for-it.sh
RUN chmod +x /usr/wait-for-it.sh

WORKDIR /app

COPY build/libs/test-project-*.jar /app/app.jar

EXPOSE 8080

CMD ["/bin/bash", "-c", "/usr/wait-for-it.sh postgres-db:5432 --strict --timeout=300 && /usr/wait-for-it.sh cassandra-db:9042 --strict --timeout=300 && java -jar app.jar"]
