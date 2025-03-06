# 使用Java 21的基础镜像
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/TransactionManager-0.0.1-SNAPSHOT.jar app.jar

# 暴露应用端口
EXPOSE 8080

# 运行应用
ENTRYPOINT ["java", "-jar", "app.jar"]