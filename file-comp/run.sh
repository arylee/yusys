#!/bin/bash
start=$(date +%s)
java -jar target/file-comp-0.0.1-SNAPSHOT-jar-with-dependencies.jar 
#python3 gen_test_data.py
end=$(date +%s)
difference=$(( end - start))
echo Time taken to execute commands is $difference seconds.
