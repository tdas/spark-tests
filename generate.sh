#/usr/env bash

cd secondary_jar_project
sbt/sbt clean package
cd ..

cd app_jar_project
sbt/sbt clean package
cd ..

zip app.zip app.py
zip secondary.zip secondary.py

