plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "1.3.72"
    id("com.gradle.plugin-publish") version "0.11.0"
}

repositories {
    jcenter()
}

dependencies {
    compileOnly(kotlin("stdlib-jdk8"))
}

gradlePlugin {
    plugins {
        create("python-gradle-plugin") {
            id = "com.pswidersk.python-plugin"
            implementationClass = "com.pswidersk.PythonPlugin"
            displayName = "Gradle plugin to run Python projects. https://github.com/PrzemyslawSwiderski/python-gradle-plugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/PrzemyslawSwiderski/python-gradle-plugin"
    vcsUrl = "https://github.com/PrzemyslawSwiderski/python-gradle-plugin"
    description = "Gradle plugin to run Python projects."
    tags = listOf("python")
}