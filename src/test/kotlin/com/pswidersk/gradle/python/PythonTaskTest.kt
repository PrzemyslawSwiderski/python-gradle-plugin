package com.pswidersk.gradle.python

import org.junit.jupiter.api.io.TempDir
import java.io.File

class PythonTaskTest {

    fun `task should run successfully`(@TempDir tempDir: File) {
        val settingsFile = tempDir.resolve("settings.gradle.kts")
        val buildFile = tempDir.resolve("build.gradle.kts")
        settingsFile.writeText("rootProject.name = \"gradle-python-project-test\"")
        buildFile.writeText(
                """
        plugins {
            id "com.jetbrains.python.envs"
        }
        
        envs {
            bootstrapDirectory = new File(buildDir, 'bootstrap')
            envsDirectory = file(buildDir)
            
            python "python-2.7.15", "2.7.15"
            virtualenv "virtualenv-2.7.15", "python-2.7.15"
        }
        
                """.trimIndent()
        )
    }

}