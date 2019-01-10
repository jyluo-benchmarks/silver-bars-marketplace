#!/bin/bash

# -Xms6g initial heap size
# -Xmx6g max heap size

CLASSPATH="./lib/unit-api-1.0.jar:./lib/uom-se-1.0.4.jar:./lib/uom-lib-common-1.0.1.jar:./lib/joda-money-0.12.jar:./target/classes/"

java -Xms6g -Xmx6g -cp $CLASSPATH \
    benchmark.BenchmarkMain
