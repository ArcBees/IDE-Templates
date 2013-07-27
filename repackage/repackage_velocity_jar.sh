#!/bin/sh
# jarjar
# https://code.google.com/p/jarjar/wiki/CommandLineDocs
# repackage velocity

mkdir -p ../target/build

java -jar jarjar.jar process rules_file velocity-1.7.jar ../target/build/velocity-1.7-repackaged.jar
