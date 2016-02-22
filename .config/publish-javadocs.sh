#!/bin/bash

if [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
  echo "Skipping Javadoc publication for pull request"
  exit
fi

if [ "$TRAVIS_TAG" == "" ]; then
  echo "Current version is not a release, skipping Javadoc publication"
  exit
fi

echo "Auto publishing latest javadocs..."
echo "TRAVIS_REPO_SLUG=$TRAVIS_REPO_SLUG - TRAVIS_JDK_VERSION=$TRAVIS_JDK_VERSION - TRAVIS_PULL_REQUEST=$TRAVIS_PULL_REQUEST"

# Decide the documentation version folder name depending on the branch and the version in the pom.xml
VERSION=`grep -m 1 "<hipster.version>" pom.xml | cut -d ">" -f 2 | cut -d "<" -f 1`

# Validate if the version is correct (example 1.0.0-SNAPSHOT, or 1.0.0-alpha-1)
VERSION_REGEX='^[0-9]+\.[0-9]+\.[0-9]+(-[a-zA-Z0-9_]+(-[0-9]+)?)?$'
if [[ $VERSION =~ $VERSION_REGEX ]]; then
  echo "Current version is $VERSION"
else
  echo "Version error. Unrecognized version $VERSION"
  exit 1
fi

echo "Deploying Hipster [$VERSION] javadocs to GitHub gh-pages"
echo "Current directory is: `pwd`"

echo "Building javadocs..."
# Generate Javadocs in target/apidocs
mvn javadoc:aggregate

# Clone Hipster4j GitHub gh-pages for Javadocs
git clone --quiet --branch=gh-pages https://github.com/hipster4j/hipster-javadocs.git gh-pages > /dev/null

# Overwrite the previous version with the new one
cp -Rf target/apidocs/* gh-pages/

# Create a new folder with the version number and copy the latest version to it
mkdir gh-pages/$VERSION
cp -Rf target/apidocs/* gh-pages/$VERSION/

# Now prepare for uploading the site to gh-pages
cd gh-pages

# Config git user and credentials
git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
git config credential.helper "store --file=.git/credentials"
echo "https://${GITHUB_TOKEN}:@github.com" > .git/credentials

git add -A
git commit -a -m "auto-commit $TRAVIS_BRANCH Hipster4j Javadocs v$VERSION (build $TRAVIS_BUILD_NUMBER)"
git push -q origin gh-pages > /dev/null
echo "Finished"

