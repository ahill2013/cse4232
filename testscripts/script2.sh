#!/bin/bash

echo "RUNNING TEST script2.sh"
echo

PORT=2135
PROJECT_NAME=Exam
NETCAT="netcat localhost ${PORT}"
DBDIR="../bin/testscripts/database"

mkdir -p ${DBDIR}

java -cp ../bin/externals/*:../bin server.Handler -p ${PORT} -d ${DBDIR}/script2Test.db > /dev/null 2>&1 &

sleep 1

echo "PROJECT_DEFINITION:${PROJECT_NAME};TASKS:2;Buy paper;2000-03-12:18h30m00s001Z;2000-03-15:18h30m00s001Z;Write exam;2020-03-15:18h30m00s001Z;2020-04-15:18h30m00s001Z"
echo "PROJECT_DEFINITION:${PROJECT_NAME};TASKS:2;Buy paper;2000-03-12:18h30m00s001Z;2000-03-15:18h30m00s001Z;Write exam;2020-03-15:18h30m00s001Z;2020-04-15:18h30m00s001Z" | ${NETCAT}
sleep 1
echo "TAKE;USER:Johny;PROJECT:${PROJECT_NAME};Buy paper"
echo "TAKE;USER:Johny;PROJECT:${PROJECT_NAME};Buy paper" | ${NETCAT}
sleep 1
echo "TAKE;USER:Mary;PROJECT:${PROJECT_NAME};Write exam"
echo "TAKE;USER:Mary;PROJECT:${PROJECT_NAME};Write exam" | ${NETCAT}
sleep 1
echo "GET_PROJECT;${PROJECT_NAME}"
echo "GET_PROJECT;${PROJECT_NAME}" | ${NETCAT}
sleep 1
echo "EXIT" | ${NETCAT}
echo "EXIT : script2.sh has finished"
