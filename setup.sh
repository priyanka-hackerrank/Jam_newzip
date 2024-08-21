#!/bin/bash

npm install
cd ..


if pgrep mongod > /dev/null; then
  echo "mongod is running"
else
  mongod --config /etc/mongod.conf --fork > /dev/null
  echo "mongod started"
fi


# Change to the backend directory
cd backend
mvn clean install

cd ..
