#!/bin/bash

PORT=2135

java -cp bin/externals/*:bin/*.class:bin:. Handler -p ${PORT} -d script1Test.db &

# SERVER_PID=$!

# netcat localhost ${PORT}

echo "GET_PROJECTS" | netcat localhost ${PORT}

echo exit | netcat localhost ${PORT}

# kill SERVER_PID
