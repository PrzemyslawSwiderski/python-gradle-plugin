import org.jetbrains.changelog.date

plugins {
    kotlin("jvm") version "1.7.10"
    id("org.jetbrains.kotlinx.kover") version "0.5.1"
    id("com.gradle.plugin-publish") version "1.0.0"
    id("net.researchgate.release") version "2.8.1"
    id("org.jetbrains.changelog") version "1.3.1"
}

repositories {
    mavenLocal()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleKotlinDsl())
    implementation("commons-io:commons-io:2.11.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("com.github.tomakehurst:wiremock-jre8:2.33.2")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks {
    test {
        useJUnitPlatform()
        testLogging.showStandardStreams = true
    }
    "afterReleaseBuild" {
        dependsOn(
            "publish",
            "publishPlugins",
            "patchChangelog"
        )
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
                "Gradle plugin to run Python projects in Conda virtual env. https://github.com/PrzemyslawSwiderski/python-gradle-plugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/PrzemyslawSwiderski/python-gradle-plugin"
    vcsUrl = "https://github.com/PrzemyslawSwiderski/python-gradle-plugin"
    description = "Gradle plugin to run Python projects."
    tags = listOf(
        "python",
        "venv",
        "numpy",
        "conda",
        "miniconda",
        "anaconda",
        "scipy",
        "pandas",
        "flask",
        "matplotlib",
        "sklearn"
    )
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