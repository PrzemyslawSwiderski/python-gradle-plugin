import org.jetbrains.changelog.date

plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm") version "1.6.21"
    id("com.gradle.plugin-publish") version "0.21.0"
    id("net.researchgate.release") version "2.8.1"
    id("org.jetbrains.changelog") version "1.3.1"
}

repositories {
    mavenLocal()
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

tasks {
    test {
        useJUnitPlatform()
        testLogging.showStandardStreams = true
    }
    "afterReleaseBuild"{
        dependsOn("publish", "publishPlugins", "patchChangelog")
    }
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}
gradlePlugin {
    plugins {
        create("python-gradle-plugin") {
            id = "com.pswidersk.python-plugin"
            implementationClass = "com.pswidersk.gradle.python.PythonPlugin"
            displayName =
                "Gradle plugin to run Python projects in Miniconda virtual env. https://github.com/PrzemyslawSwiderski/python-gradle-plugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/PrzemyslawSwiderski/python-gradle-plugin"
    vcsUrl = "https://github.com/PrzemyslawSwiderski/python-gradle-plugin"
    description = "Gradle plugin to run Python projects."
    tags = listOf("python", "venv", "numpy", "miniconda", "conda", "scipy", "pandas")
}

publishing {
    repositories {
        mavenLocal()
    }
}

// Configuring changelog Gradle plugin https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    header.set(provider { "[${version.get()}] - ${date()}" })
}