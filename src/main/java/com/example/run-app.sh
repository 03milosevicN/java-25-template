#!/bin/zsh

mvn -f ../../../../../pom.xml \
    exec:java \
    -Dexec.mainClass=com.example.App \
    -Dexec.args=$@ \
    -q