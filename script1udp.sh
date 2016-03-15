#!/bin/bash

echo "RUNNING TEST script1udp.sh"
echo

PORT=2135

echo "javac -d bin/ src/UDPClient.java"
javac -d bin/ src/UDPClient.java
sleep 1

echo "java -cp bin/externals/*:bin/*.class:bin Handler -p ${PORT} -d script1udpTest.db > /dev/null 2>&1 &"
java -cp bin/externals/*:bin/*.class:bin Handler -p ${PORT} -d script1udpTest.db > /dev/null 2>&1 &

echo "java -cp bin UDPClient"
echo
java -cp bin UDPClient ${PORT} 1
echo "EXIT : script1udp.sh has finished"
