#!/bin/bash

echo "Auto-deploying artifacts..."
echo "Current branch: $TRAVIS_BRANCH"

if [ "$TRAVIS_REPO_SLUG" == "citiususc/hipster" ] && [ "$TRAVIS_JDK_VERSION" == "oraclejdk7" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
echo "Running mvn deploy..."
  mvn deploy --settings .config/maven-settings.xml -DskipTests=true
else
echo "Skipping deployment for this build..."
fi

echo "Deployment script finished."