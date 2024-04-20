FROM openjdk:21
COPY ./scrapper/target/scrapper.jar scrapper.jar
EXPOSE 8080
EXPOSE 8081
ENTRYPOINT ["java","-jar","/scrapper.jar"]
