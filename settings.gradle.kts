rootProject.name = "python-gradle-plugin"

include("examples:sample-python-project",
        "examples:geobuf-python-project",
        "examples:sample-python-project-groovy-dsl")

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