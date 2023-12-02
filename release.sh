#!/bin/bash -e

export RELEASE_VERSION=$1
export DEVELOPMENT_VERSION=$2

if [[ -z "$RELEASE_VERSION" ]]; then
  echo "Error: Release version is undefined"
  exit 1

fi
if [[ -z "$DEVELOPMENT_VERSION" ]]; then
  echo "Error: Next development version is undefined"
  exit 1
fi

if [[ -z $(git status -s) ]]
then
  echo "Git tree is clean"
else
  echo "Git tree is dirty, please commit changes before running this"
  exit
fi


echo ">>>>>> Releasing version $RELEASE_VERSION <<<<<<"
mvn versions:set-property -Dproperty=revision -DnewVersion=$RELEASE_VERSION -DgenerateBackupPoms=false
# active -Pmaven-central profile cases than only deployable modules are build, hence mvn clean install before that
mvn clean install
mvn clean deploy -Ddeployable-modules-only -Prelease -Pmaven-central -s ~/.m2/settings-central.xml
#mvn clean deploy -Prelease -Padgadev-ssh

git add pom.xml
git commit -m "Release $RELEASE_VERSION"
git tag $RELEASE_VERSION
git push --tags

echo ">>>>>> Next development version will be set to $DEVELOPMENT_VERSION <<<<<<<"
mvn versions:set-property -Dproperty=revision -DnewVersion=$DEVELOPMENT_VERSION -DgenerateBackupPoms=false
git add pom.xml
git commit -m "Next development version"
git push origin master

echo ">>>>> Release finished successfully <<<<<<<<"
