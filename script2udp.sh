#!/bin/bash

echo "RUNNING TEST script2udp.sh"
echo

PORT=2135

echo "javac -d bin/ src/client/UDPClient.java"
javac -d bin/ src/client/UDPClient.java
sleep 1

echo "java -cp bin/externals/*:bin server.Handler -p ${PORT} -d script2udpTest.db > /dev/null 2>&1 &"
java -cp bin/externals/*:bin server.Handler -p ${PORT} -d script2udpTest.db > /dev/null 2>&1 &

echo "java -cp bin client.UDPClient"
echo
java -cp bin client.UDPClient ${PORT} 2
echo "EXIT : script2udp.sh has finished"
