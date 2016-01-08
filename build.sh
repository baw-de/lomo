#!/bin/sh

find src/ -name "*.java" > sources.txt
javac -cp lib/controlsfx/controlsfx-8.40.10.jar;src/ @sources.txt