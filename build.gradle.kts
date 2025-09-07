import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(libs.bundles.jupiter)
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
    withType<KotlinCompile> {
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
            displayName = "Plugin to run Python projects in Conda virtual env. "
                .plus("https://github.com/PrzemyslawSwiderski/python-gradle-plugin")
            description = "Plugin to setup Python and run scripts in Conda virtual environments."
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

kover {
    reports {
        total {
            xml {
                onCheck = true
            }
        }
    }
}
