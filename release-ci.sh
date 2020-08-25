#!/bin/bash -e

export RELEASE_VERSION=$1

if [[ -z "$RELEASE_VERSION" ]]; then
  echo "Error: Release version is undefined"
  exit 1

fi

echo ">>>>>> Releasing version $RELEASE_VERSION <<<<<<"
mvn3 versions:set-property -Dproperty=revision -DnewVersion=$RELEASE_VERSION -DgenerateBackupPoms=false
#mvn3 clean deploy -Prelease -Pgrexdev-ssh
#mvn3 clean deploy -Prelease -Pmaven-central -s ~/.m2/settings-central.xml

git add pom.xml
git commit -m "Release $RELEASE_VERSION"
git tag $RELEASE_VERSION
git push --tags

git reset HEAD~1
git checkout .
#git clean -fd

echo ">>>>> Release finished successfully <<<<<<<<"
