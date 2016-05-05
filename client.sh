#!/bin/bash

if [ $# -eq  2 ]
then
	echo "java -cp bin/externals/*:bin client.Client $1 $2"
	java -cp bin/externals/*:bin client.Client $1 $2
elif [ $# -eq  3 ]
then
	echo "java -cp bin/externals/*:bin client.Client $1 $2 $3"
	java -cp bin/externals/*:bin client.Client $1 $2 $3
elif [ $# -eq 4 ]
then
    echo "java -cp bin/externals/*:bin client.Client $1 $2 $3 $4"
    java -cp bin/externals/*:bin client.Client $1 $2 $3 $4
else
	echo "Usage: client.sh <IP> <port> <optional project name> <optional_flag>" >&2
fi