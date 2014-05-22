#!/bin/bash

#TRAVIS_REPO_SLUG="citiususc/hipster"
#TRAVIS_JDK_VERSION="oraclejdk7"
#TRAVIS_PULL_REQUEST="false"
#TRAVIS_BRANCH="master"
#HOME=`pwd`

echo "Preparing Hipster site for auto-deploy"
echo "TRAVIS_REPO_SLUG=$TRAVIS_REPO_SLUG - TRAVIS_JDK_VERSION=$TRAVIS_JDK_VERSION - TRAVIS_PULL_REQUEST=$TRAVIS_PULL_REQUEST"

if [ "$TRAVIS_REPO_SLUG" == "citiususc/hipster" ] && [ "$TRAVIS_JDK_VERSION" == "oraclejdk7" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then

  # Decide the documentation version folder name depending on the branch and the version in the pom.xml
  # wget https://raw.githubusercontent.com/citiususc/hipster/$TRAVIS_BRANCH/pom.xml > /dev/null 2>&1
  # Take the version from the main pom.xml
  # grep -m 1 -E '<version>[0-9]+\.[0-9]+\.[0-9]+(-[a-zA-Z0-9_]+(-[0-9]+)?)?</version>' pom.xml
  VERSION=`grep -m 1 "<hipster.version>" pom.xml | cut -d ">" -f 2 | cut -d "<" -f 1`
  # rm pom.xml
  
  # Validate if the version is correct (example 1.0.0-SNAPSHOT, or 1.0.0-alpha-1)
  VERSION_REGEX='^[0-9]+\.[0-9]+\.[0-9]+(-[a-zA-Z0-9_]+(-[0-9]+)?)?$'
  if [[ $VERSION =~ $VERSION_REGEX ]]; then
    echo "Current version is $VERSION"
  else
    echo "Version error. Unrecognized version $VERSION"
    exit 1
  fi
  
  echo "Deploying Hipster [$VERSION] site and documentation to GitHub gh-pages"
  echo "Current directory is: `pwd`"


  echo "Building javadocs..."
  mvn javadoc:aggregate

  # Build site only if this is the master branch
  if [ "$TRAVIS_BRANCH" == "master" ]; then
    echo "Building site..."
    mvn site:site
  fi

  # First, copy the generated site to the new folder
  mkdir $HOME/site
  cp -Rf target/site/* $HOME/site
  # Remove the apidocs site and use the aggregated javadoc instead
  rm -rf $HOME/site/apidocs
  # Copy the apidocs to the site folder
  mkdir $HOME/site/documentation
  mkdir $HOME/site/documentation/javadoc
  mkdir $HOME/site/documentation/javadoc/$VERSION
  cp -Rf target/apidocs/* $HOME/site/documentation/javadoc/$VERSION
  
  # Now prepare for uploading the site to gh-pages
  
  cd $HOME
  git clone --quiet --branch=gh-pages https://github.com/citiususc/hipster.git gh-pages > /dev/null
  
  # Copy and overwrite the site with the new content
  cp -Rf $HOME/site/* gh-pages/
  cd gh-pages

  # Config git user and credentials
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
  git config credential.helper "store --file=.git/credentials"
  echo "https://${GITHUB_TOKEN}:@github.com" > .git/credentials
  
  git add -A
  git commit -a -m "auto-commit $TRAVIS_BRANCH Hipster site updated (build $TRAVIS_BUILD_NUMBER)"
  git push -q origin gh-pages > /dev/null
  echo "Published $TRAVIS_BRANCH Hipster site to gh-pages."

fi  
