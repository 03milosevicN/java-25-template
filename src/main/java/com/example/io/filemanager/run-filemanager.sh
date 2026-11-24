#!/bin/zsh

mvn -f ../../../../../../pom.xml \
    exec:java \
    -Dexec.mainClass=com.example.filemanager.Main \
    -Dexec.args=$@ \
    -q