# Первый этап сборки
FROM openjdk:21-jdk AS builder
WORKDIR /app
ARG JAR_FILE=build/libs/delivery-service-0.1.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Второй этап сборки
FROM openjdk:21-jdk
ENV PORT=8082
WORKDIR /app
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]