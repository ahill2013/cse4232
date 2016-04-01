#!/bin/bash

echo "RUNNING TEST script1udp.sh"
echo

PORT=2135
DBDIR="../bin/testscripts/database"

mkdir -p ${DBDIR}

echo "javac -d ../bin/ ../src/client/UDPClient.java"
javac -d ../bin/ ../src/client/UDPClient.java
sleep 1

echo "java -cp ../bin/externals/*:../bin server.Handler -p ${PORT} -d script1udpTest.db > /dev/null 2>&1 &"
java -cp ../bin/externals/*:../bin server.Handler -p ${PORT} -d ${DBDIR}/script1udpTest.db > /dev/null 2>&1 &

echo "java -cp ../bin client.UDPClient"
echo
java -cp ../bin client.UDPClient ${PORT} 1
echo "EXIT : script1udp.sh has finished"
