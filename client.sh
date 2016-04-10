#!/bin/bash

if [ $# -eq  2 ]
then
	echo "java -cp bin/externals/*:bin client.Client $1 $2"
	java -cp bin/externals/*:bin client.Client $1 $2
elif [ $# -eq  3 ]
then
	echo "java -cp bin/externals/*:bin client.Client $1 $2 $3"
	java -cp bin/externals/*:bin client.Client $1 $2 $3
else
	echo "Usage: client.sh <IP> <port> <optional_flag>" >&2
fi