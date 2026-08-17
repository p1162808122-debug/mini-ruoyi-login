#!/usr/bin/env bash
set -e

cd "$(dirname "$0")"

JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
MAVEN=/SSD2/pengzhipeng/anaconda3/opt/maven/bin/mvn
REPOSITORY=/SSD2/pengzhipeng/.m2/repository

if [ ! -x "$JAVA_HOME/bin/java" ]; then
  echo "没有找到 Java 21：$JAVA_HOME/bin/java"
  exit 1
fi

if [ ! -x "$MAVEN" ]; then
  echo "没有找到 Maven：$MAVEN"
  exit 1
fi

export JAVA_HOME
export PATH="$JAVA_HOME/bin:$PATH"

exec "$MAVEN" -Dmaven.repo.local="$REPOSITORY" spring-boot:run

