#!/bin/bash

if [ "$#" -ne 2 ]
then
  echo "Usage: $0 <version> <dev or release>"
  echo "Script to set project versions. Supports Maven, NPM/node, Cordova."
  echo "Use 'release' when making a release or 'dev' when setting the next development version."
  exit 1
fi

version=$1
type=$2 # dev or release


npm_version=$version
mvn_version=$version
cordova_version=$version

if [ "$type" = "dev" ]; then
    npm_version=${version}-alpha
    mvn_version=${version}-SNAPSHOT
fi

if [ -f "package.json" ]; then
  echo "NPM/Node project detected. Setting version to: $npm_version"
  npm version "$npm_version" --allow-same-version=true --git-tag-version=false
fi

if [ -f "config.xml" ]; then
  echo "Cordova project detected. Setting version to: $cordova_version"
  node scripts/config-xml-version-changer.js "${cordova_version}" ${type}
fi

if [ -f "pom.xml" ]; then
  echo "Maven project detected. Setting version to: $mvn_version"
  ./mvnw versions:set -DnewVersion="$mvn_version" -DgenerateBackupPoms=false
fi

echo "Done!"
