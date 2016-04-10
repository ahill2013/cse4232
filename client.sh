#!/bin/bash
if [ $# -eq 0 ]
then
	echo "Usage: client.sh <IP> <port> <optional_tag>" >&2
elif [ $# -eq 1 ]
then
	echo "Usage: client.sh <IP> <port> <optional_tag>" >&2
elif [ $# -eq  2 ]
then
	java -cp bin/externals/*:bin client.Client $1 $2
elif [ $# -eq  3 ]
then
	#if [ $1 -eq  ]
	java -cp bin/externals/*:bin client.Client $1 $2 $3
fi