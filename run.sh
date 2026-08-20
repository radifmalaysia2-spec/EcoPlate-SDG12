#!/usr/bin/env bash
set -euo pipefail
mkdir -p out
find src -name '*.java' -print0 | xargs -0 javac -encoding UTF-8 -d out
java -cp out com.cityu.ecoplate.App
