FROM eclipse-temurin:17-jdk-jammy
ARG JAR_FILE=target/Tp_Ecommerce-0.0.1.jar
COPY ${JAR_FILE} Tp_Ecommerce.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/Tp_Ecommerce.jar"]