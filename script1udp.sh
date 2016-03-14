#!/bin/bash

echo "RUNNING TEST script1udp.sh"
echo

PORT=2135

javac -d bin/ src/UDPClient.java

java -cp bin/externals/*:bin/*.class:bin:. Handler -p ${PORT} -d script2udpTest.db &
