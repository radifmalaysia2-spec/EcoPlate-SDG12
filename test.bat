@echo off
if not exist out-test mkdir out-test
dir /s /b src\*.java test\*.java > sources-test.txt
javac -encoding UTF-8 -d out-test @sources-test.txt
del sources-test.txt
java -ea -cp out-test com.cityu.ecoplate.EcoPlateTest
