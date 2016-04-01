#!/bin/bash

DEFAULTPORT=2132

if [ $# -eq 0 ]
then
    # echo "Using default port number: ${DEFAULTPORT}"
    java -cp bin/externals/*:bin server.Handler -p ${DEFAULTPORT}
elif [ $# -eq 1 ]
then
    # echo "Using port number: ${1}"
    java -cp bin/externals/*:bin server.Handler -p $1
elif [ $# -eq 2 ]
then
#    if [ $1 = "-p" ]
#    then
#        # echo "Using port number: ${1}"
        java -cp bin/externals/*:bin server.Handler $1 $2
#    elif [ $1 = "-d" ]
#    then
#        # echo "Using default port number: ${DEFAULTPORT}"
#        java -cp bin/externals/*:bin server.Handler -p ${DEFAULTPORT} -d $2
#    fi
elif [ $# -eq 4 ]
then
    if [ $1 = "-p" ]
    then
        echo "Using port number: ${2}"
    elif [ $3 = "-p" ]
    then
        echo "Using port number: ${4}"
    fi
    java -cp bin/externals/*:bin server.Handler $1 $2 $3 $4
    exit 0
else
    echo "Sample usage: run.sh <port number>" >&2
    echo "- OR -" >&2
    echo "Sample usage: run.sh -p <portnumber> -d <path/to/file/location.db>" >&2
    exit 1
fi
