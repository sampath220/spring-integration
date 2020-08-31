FROM registry.transplace.com/tplace/alpine-java8-newrelic:latest

RUN mkdir /app

ADD target/*.jar /app/application.jar

EXPOSE 8080

CMD java $JAVA_OPTS -jar /app/application.jar
