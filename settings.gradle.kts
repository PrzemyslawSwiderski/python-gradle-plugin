rootProject.name = "python-gradle-plugin"

include("examples:sample-python-project", "examples:geobuf-python-project")

pluginManagement {
    repositories {
        mavenLocal()
        jcenter()
        maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
    }
}