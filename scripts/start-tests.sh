#!/bin/bash
set -x

Xvfb -ac :99 -screen 0 1280x1024x16 & export DISPLAY=:99
cd ${WORKSPACE}

#./gradlew balanceTests

./gradlew smokeTests
retVal=$?
if [ $retVal -ne 0 ]; then
exit $retVal
fi

./gradlew clean build
exit 0