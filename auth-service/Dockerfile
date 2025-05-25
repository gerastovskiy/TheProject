# 1 stage
FROM openjdk:21-jdk as builder
WORKDIR app
ARG JAR_FILE=build/libs/auth-service-0.1.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# 2 stage
FROM openjdk:21-jdk
WORKDIR app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]