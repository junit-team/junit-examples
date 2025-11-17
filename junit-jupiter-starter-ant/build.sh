#!/usr/bin/env bash
set -e

#
# Set constants.
#
junit_version='6.1.0-M1'
ant_version='1.10.15'
ant_folder="apache-ant-${ant_version}"
ant_archive="${ant_folder}-bin.tar.gz"
standalone_jar="${ant_folder}/lib/junit-platform-console-standalone-${junit_version}.jar"

#
# Load and extract Apache Ant.
#
if [ ! -d "${ant_folder}" ]; then
    echo "Downloading Ant $ant_version..."
    curl --silent --show-error --fail --remote-name "https://archive.apache.org/dist/ant/binaries/${ant_archive}"
    tar --extract -z --exclude "${ant_folder}/manual" --file "${ant_archive}"
fi

#
# Load and store junit-platform-console-standalone jar into ${ANT_HOME}/lib.
#
if [ ! -f "${standalone_jar}" ]; then
    echo "Downloading junit-platform-console-standalone $junit_version..."
    curl --silent --show-error --fail "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/${junit_version}/junit-platform-console-standalone-${junit_version}.jar" \
         --output "${standalone_jar}"
fi

#
# Finally, let Ant do its work...
#
ANT_HOME=${ant_folder} "./${ant_folder}/bin/ant" "$@"
