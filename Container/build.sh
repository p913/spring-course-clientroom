#!/bin/bash

mkdir jar

sudo apt-get install git maven

if [ -d source ]; then
cd source
git pull
else
git clone https://github.com/p913/spring-course-clientroom.git --branch dev source
cd source
fi

if [ -d waitfor ]; then
else
git clone https://github.com/vishnubob/wait-for-it.git waitfor
fi

mvn install -f RoomApiLib/pom.xml  -D skipTests
mvn package -f RoomApiService/pom.xml  -D skipTests
mvn package -f RoomService/pom.xml  -D skipTests
mvn package -f ConfigServer/pom.xml  -D skipTests
mvn package -f ServiceDiscoveryServer/pom.xml  -D skipTests
mvn package -f SmsEmulator/pom.xml  -D skipTests
mvn package -f UseApiExamples/pom.xml  -D skipTests

mkdir jar

mv RoomService/target/*.jar ../jar/client-room.jar
mv RoomApiService/target/*.jar ../jar/client-room-api.jar 
mv ConfigServer/target/*.jar ../jar/config-server.jar
mv ServiceDiscoveryServer/target/*.jar ../jar/discovery-server.jar
mv SmsEmulator/target/*.jar ../jar/sms-emulator.jar
mv UseApiExamples/target/*.jar ../jar/use-api-examples.jar

sudo docker-compose build

sudo docker-compose up -d --scale client-room-api=2

