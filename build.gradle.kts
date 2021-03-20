import org.jetbrains.changelog.closure
import org.jetbrains.changelog.date

plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm") version "1.4.31"
    id("com.gradle.plugin-publish") version "0.11.0"
    id("net.researchgate.release") version "2.8.1"
    id("org.jetbrains.changelog") version "0.5.0"
}

repositories {
    mavenLocal()
    jcenter()
    maven {
        setUrl("https://plugins.gradle.org/m2/")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
}

tasks {
    test {
        useJUnitPlatform()
        testLogging.showStandardStreams = true
    }
    "afterReleaseBuild"{
        dependsOn("publish", "publishPlugins", "patchChangelog")
    }
}
gradlePlugin {
    plugins {
        create("python-gradle-plugin") {
            id = "com.pswidersk.python-plugin"
            implementationClass = "com.pswidersk.gradle.python.PythonPlugin"
            displayName = "Gradle plugin to run Python projects in Miniconda virtual env. https://github.com/PrzemyslawSwiderski/python-gradle-plugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/PrzemyslawSwiderski/python-gradle-plugin"
    vcsUrl = "https://github.com/PrzemyslawSwiderski/python-gradle-plugin"
    description = "Gradle plugin to run Python projects."
    tags = listOf("python", "venv", "numpy", "miniconda", "conda")
}

publishing {
    repositories {
        mavenLocal()
    }
}

changelog {
    header = closure { "[${project.version}] - ${date()}" }
}