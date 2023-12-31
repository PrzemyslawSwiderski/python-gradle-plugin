import org.jetbrains.changelog.date
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "1.9.10"
    id("org.jetbrains.kotlinx.kover") version "0.7.3"
    id("com.gradle.plugin-publish") version "1.2.1"
    id("net.researchgate.release") version "2.8.1"
    id("org.jetbrains.changelog") version "1.3.1"
}

repositories {
    mavenLocal()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleKotlinDsl())
    implementation("commons-io:commons-io:2.15.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.1")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.wiremock:wiremock:3.3.1")
}

kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

tasks {
    test {
        useJUnitPlatform()
        testLogging.showStandardStreams = true
    }
    afterReleaseBuild {
        dependsOn(
            "publish",
            "publishPlugins",
            "patchChangelog"
        )
    }
    compileKotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
}

gradlePlugin {
    website = "https://github.com/PrzemyslawSwiderski/python-gradle-plugin"
    vcsUrl = "https://github.com/PrzemyslawSwiderski/python-gradle-plugin"
    plugins {
        create("python-gradle-plugin") {
            id = "com.pswidersk.python-plugin"
            implementationClass = "com.pswidersk.gradle.python.PythonPlugin"
            displayName = "Gradle plugin to run Python projects in Conda virtual env. "
                .plus("https://github.com/PrzemyslawSwiderski/python-gradle-plugin")
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
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}

// Configuring changelog Gradle plugin https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    header = provider { "[${version.get()}] - ${date()}" }
}