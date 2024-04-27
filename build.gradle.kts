import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kover)
    alias(libs.plugins.pluginPublish)
    alias(libs.plugins.changelog)
}

repositories {
    mavenLocal()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleKotlinDsl())
    implementation(libs.commonsIO)
    implementation(libs.snakeyaml)
    testImplementation(libs.jupiter)
    testImplementation(libs.jupiterParams)
    testImplementation(libs.assertj)
    testImplementation(libs.wiremock)
}

java {
    targetCompatibility = JavaVersion.VERSION_11
}

tasks {
    test {
        useJUnitPlatform()
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
    groups = listOf("Added", "Changed", "Removed")
}
