rootProject.name = "python-gradle-plugin"

include("examples:sample-python-project",
        "examples:geobuf-python-project",
        "examples:sample-python-project-groovy-dsl")

pluginManagement {
    repositories {
        mavenLocal()
        jcenter()
        maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
    }
}