#!/bin/bash

if [ $# -lt 1 ]
then
    echo "Usage: run.sh <port number>" >&2
    exit 1
fi
java -cp bin/externals/*:bin/*.class:bin:. Handler $1 $2 $3 $4
