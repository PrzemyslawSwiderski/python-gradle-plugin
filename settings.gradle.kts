rootProject.name = "python-gradle-plugin"

include(
    "examples:geobuf-python-project",
    "examples:home-dir-sample",
    "examples:sample-anaconda-project",
    "examples:sample-flask-project",
    "examples:sample-python-project",
    "examples:sample-python-project-groovy-dsl",
    "examples:sample-tts-project"
)

pluginManagement {
    val pythonPluginVersionForExamples: String by settings

    plugins {
        id("com.pswidersk.python-plugin") version pythonPluginVersionForExamples
    }

    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
