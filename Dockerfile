FROM openjdk:17
ADD ./build/libs/surveyresultserver-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=deploy", "app.jar"]