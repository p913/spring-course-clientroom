FROM openjdk:11-jdk

ARG jarname

WORKDIR /opt/app/

COPY jar/$jarname.jar .

COPY waitfor/wait-for-it.sh .

