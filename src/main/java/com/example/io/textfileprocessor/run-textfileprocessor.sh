#!/bin/zsh

mvn -f ../../../../../../pom.xml \
    exec:java \
    -Dexec.mainClass=com.example.textfileprocessor.Main \
    -q