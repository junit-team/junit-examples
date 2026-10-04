load("@rules_java//java:java_library.bzl", "java_library")
load("@rules_jvm_external//:defs.bzl", "artifact")
load("@contrib_rules_jvm//java:defs.bzl", "java_test_suite")

java_library(
    name = "junit-jupiter-starter-bazel",
    srcs = glob(["src/main/java/**/*.java"]),
)

java_test_suite(
    name = "junit-jupiter-starter-bazel-tests",
    size = "small",
    srcs = glob(["src/test/java/**/*.java"]),
    runner = "junit5",
    test_suffixes = ["Tests.java"],
    runtime_deps = [
        artifact("org.junit.jupiter:junit-jupiter-engine"),
        artifact("org.junit.platform:junit-platform-launcher"),
        artifact("org.junit.platform:junit-platform-reporting"),
    ],
    deps = [
        ":junit-jupiter-starter-bazel",
        artifact("org.junit.jupiter:junit-jupiter-api"),
        artifact("org.junit.jupiter:junit-jupiter-params"),
    ],
)
