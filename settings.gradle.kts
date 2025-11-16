rootProject.name = "python-gradle-plugin"

include(
    "examples:geobuf-python-project",
    "examples:home-dir-sample",
    "examples:sample-mamba-project",
    "examples:sample-flask-project",
    "examples:sample-python-project",
    "examples:sample-python-project-groovy-dsl",
    "examples:sample-tts-project"
)

pluginManagement {
    val pluginVersionForExamples: String by settings

    plugins {
        id("com.pswidersk.python-plugin") version pluginVersionForExamples
    }

    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
