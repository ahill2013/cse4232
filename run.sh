#!/bin/bash

java -cp bin/externals/*:bin/*.class:bin:. Handler $1 $2 $3 $4
