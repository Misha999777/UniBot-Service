FROM ubuntu:20.04
RUN apt-get update
RUN apt-get install -y openjdk-11-jdk
COPY target/UniBot.jar /usr/app/UniBot.jar
CMD java -jar /usr/app/UniBot.jar