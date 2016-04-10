#!/bin/bash

echo "RUNNING TEST script1ASN_TCP.sh"
echo

PORT=2135
PROJECT_NAME=Exam2
DBDIR="../bin/testscripts/database"

mkdir -p ${DBDIR}

echo "java -cp ../bin/externals/*:../bin server.Handler -p ${PORT} -d ${DBDIR}/script1ASN_TCP.db &"
java -cp ../bin/externals/*:../bin server.Handler -p ${PORT} -d ${DBDIR}/script1ASN_TCP.db &

sleep 1

echo "PROJECT_DEFINITION:${PROJECT_NAME};TASKS:2;Buy paper;2016-03-12:18h30m00s001Z;2014-03-15:18h30m00s001Z;Write exam;2016-03-15:18h30m00s001Z;2014-03-15:18h30m00s001Z" > tmpinput.txt
echo "TAKE;USER:Johny;PROJECT:${PROJECT_NAME};Buy paper" >> tmpinput.txt
echo "TAKE;USER:Mary;PROJECT:${PROJECT_NAME};Write exam" >> tmpinput.txt
echo "GET_PROJECTS" >> tmpinput.txt
echo "GET_PROJECT;${PROJECT_NAME}" >> tmpinput.txt

echo "java -cp ../bin/externals/*:../bin client.Client 127.0.0.1 ${PORT} < tmpinput.txt"
java -cp ../bin/externals/*:../bin client.Client 127.0.0.1 ${PORT} < tmpinput.txt
echo "EXIT : script1ASN_TCP.sh has finished"
