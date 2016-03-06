#!/bin/bash

echo "RUNNING TEST script1.sh"
echo

PORT=2135
PROJECT_NAME=Exam2

java -cp bin/externals/*:bin/*.class:bin:. Handler -p ${PORT} -d script1Test.db &

sleep 1

echo "GET_PROJECTS"
echo "GET_PROJECTS" | netcat localhost ${PORT}
sleep 1
echo "PROJECT_DEFINITION:${PROJECT_NAME};TASKS:2;Buy paper;2016-03-12:18h30m00s001Z;2014-03-15:18h30m00s001Z;Write exam;2016-03-15:18h30m00s001Z;2014-03-15:18h30m00s001Z"
echo "PROJECT_DEFINITION:${PROJECT_NAME};TASKS:2;Buy paper;2016-03-12:18h30m00s001Z;2014-03-15:18h30m00s001Z;Write exam;2016-03-15:18h30m00s001Z;2014-03-15:18h30m00s001Z" | netcat localhost ${PORT}
sleep 1
echo "TAKE;USER:Johny;PROJECT:${PROJECT_NAME};Buy paper"
echo "TAKE;USER:Johny;PROJECT:${PROJECT_NAME};Buy paper" | netcat localhost ${PORT}
sleep 1
echo "GET_PROJECTS"
echo "GET_PROJECTS" | netcat localhost ${PORT}
sleep 1
echo "GET_PROJECT;${PROJECT_NAME}"
echo "GET_PROJECT;${PROJECT_NAME}" | netcat localhost ${PORT}
sleep 1
echo "EXIT"
echo "EXIT" | netcat localhost ${PORT}
