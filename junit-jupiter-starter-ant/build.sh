#!/usr/bin/env bash
set -e

#
# Set constants.
#
ivy_version='2.4.0'
ant_version='1.10.15'
ant_folder="apache-ant-${ant_version}"
ant_archive="${ant_folder}-bin.tar.gz"

#
# Load and extract Apache Ant.
#
curl --output "${ant_archive}" "https://archive.apache.org/dist/ant/binaries/${ant_archive}"
tar --extract -z --exclude "${ant_folder}/manual" --file "${ant_archive}"

#
# Load and store ivy jar into ${ANT_HOME}/lib.
#
curl --output "${ant_folder}/lib/ivy-${ivy_version}.jar" "https://repo.maven.apache.org/maven2/org/apache/ivy/ivy/${ivy_version}/ivy-${ivy_version}.jar"

#
# Finally, let Ant do its work...
#
ANT_HOME=${ant_folder} "./${ant_folder}/bin/ant" "$@"
