#!/usr/bin/env bash
set -e

#
# Set constants.
#
ant_version='1.10.15'
ant_folder="apache-ant-${ant_version}"
ant_archive="${ant_folder}-bin.tar.gz"

#
# Load and extract Apache Ant.
#
curl --output "${ant_archive}" "https://archive.apache.org/dist/ant/binaries/${ant_archive}"
tar --extract -z --exclude "${ant_folder}/manual" --file "${ant_archive}"

#
# Finally, let Ant do its work...
#
ANT_HOME=${ant_folder} "./${ant_folder}/bin/ant" "$@"
