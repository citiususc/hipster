#!/bin/bash

echo "Auto-deploying Hipster4j snapshots..."
echo "Current branch: $TRAVIS_BRANCH"
if [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
  echo "Skipping snapshot deployment for pull request"
  exit
fi
echo "Running mvn deploy, current directory: `pwd`"
# Deploy to Sonatype Nexus OSS
mvn --settings .config/maven-settings.xml -P sonatype-nexus-snapshots deploy -DskipTests=true
echo "Deployment script finished." 
