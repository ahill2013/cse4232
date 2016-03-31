#!/bin/bash

echo "RUNNING TEST script1.sh"
echo

PORT=2135
PROJECT_NAME=Exam2
NETCAT="netcat localhost ${PORT}"

java -cp bin/externals/*:bin server.Handler -p ${PORT} -d script1Test.db > /dev/null 2>&1 &

sleep 1

echo "GET_PROJECTS"
echo "GET_PROJECTS" | ${NETCAT}
sleep 1
echo "PROJECT_DEFINITION:${PROJECT_NAME};TASKS:2;Buy paper;2016-03-12:18h30m00s001Z;2014-03-15:18h30m00s001Z;Write exam;2016-03-15:18h30m00s001Z;2014-03-15:18h30m00s001Z"
echo "PROJECT_DEFINITION:${PROJECT_NAME};TASKS:2;Buy paper;2016-03-12:18h30m00s001Z;2014-03-15:18h30m00s001Z;Write exam;2016-03-15:18h30m00s001Z;2014-03-15:18h30m00s001Z" | ${NETCAT}
sleep 1
echo "TAKE;USER:Johny;PROJECT:${PROJECT_NAME};Buy paper"
echo "TAKE;USER:Johny;PROJECT:${PROJECT_NAME};Buy paper" | ${NETCAT}
sleep 1
echo "TAKE;USER:Mary;PROJECT:${PROJECT_NAME};Write exam"
echo "TAKE;USER:Mary;PROJECT:${PROJECT_NAME};Write exam" | ${NETCAT}
sleep 1
echo "GET_PROJECTS"
echo "GET_PROJECTS" | ${NETCAT}
sleep 1
echo "GET_PROJECT;${PROJECT_NAME}"
echo "GET_PROJECT;${PROJECT_NAME}" | ${NETCAT}
sleep 1
echo "EXIT" | ${NETCAT}
echo "EXIT : script1.sh has finished"
