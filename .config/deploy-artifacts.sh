#!/bin/bash

echo "Auto-deploying Hipster artifacts..."
echo "Current branch: $TRAVIS_BRANCH"

if [ "$TRAVIS_REPO_SLUG" == "citiususc/hipster" ] && [ "$TRAVIS_JDK_VERSION" == "oraclejdk7" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
  echo "Running mvn deploy, current directory: `pwd`"
  # Deploy to CITIUS
  #mvn --settings .config/maven-settings.xml -P citius-snapshot-deploy deploy -DskipTests=true
  # Deploy to Sonatype Nexus OSS
  mvn --settings .config/maven-settings.xml -P sonatype-nexus-snapshots deploy -DskipTests=true
else
  echo "Skipping deployment for this build..."
fi

echo "Deployment script finished." 
