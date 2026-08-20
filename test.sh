#!/usr/bin/env bash
set -euo pipefail
mkdir -p out-test
find src test -name '*.java' -print0 | xargs -0 javac -encoding UTF-8 -d out-test
java -ea -cp out-test com.cityu.ecoplate.EcoPlateTest
