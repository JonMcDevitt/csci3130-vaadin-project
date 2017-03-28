#!/bin/sh
mvn -B -e -V -Dvaadin.testbench.developer.license=$TESTBENCH_LICENSE verify
echo vaadin.testbench.developer.license