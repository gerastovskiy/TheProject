# Первый этап сборки
FROM openjdk:21-jdk-slim-buster AS builder
WORKDIR /app
ARG JAR_FILE=build/libs/order-service-0.1.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --destination extracted

# Второй этап сборки
FROM openjdk:21-jdk-slim-buster
ENV PORT=8083
WORKDIR /app
COPY --from=builder app/extracted/dependencies/ ./
COPY --from=builder app/extracted/spring-boot-loader/ ./
COPY --from=builder app/extracted/snapshot-dependencies/ ./
COPY --from=builder app/extracted/application/ ./
ENTRYPOINT ["java", "-jar", "app.jar"]