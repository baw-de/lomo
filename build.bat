dir /s /B src\*.java > sources.txt
javac -cp lib/controlsfx/controlsfx-8.40.12.jar;src/ @sources.txt