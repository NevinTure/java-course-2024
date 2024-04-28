FROM openjdk:21
COPY ./bot/target/bot.jar /bot.jar
EXPOSE 8090
EXPOSE 8091
ENTRYPOINT ["java","-jar","/bot.jar"]
