package com.pswidersk.gradle.python

import javax.inject.Inject
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.property

abstract class PythonPluginExtension @Inject constructor(
    project: Project,
    objects: ObjectFactory,
) {

    @get:Input
    val pythonVersion: Property<String> =
        objects.property<String>().convention(DEFAULT_PYTHON_VERSION)

    @get:Input
    val minicondaVersion: Property<String> =
        objects.property<String>().convention(DEFAULT_MINICONDA_VERSION)

    @get:Input
    val installDir: DirectoryProperty = objects.directoryProperty().convention(
        project.layout.projectDirectory.dir(GRADLE_FILES_DIR).dir(PYTHON_ENVS_DIR)
    )

    @get:Input
    internal val minicondaDir: DirectoryProperty = objects.directoryProperty().convention(
        installDir.zip(minicondaVersion) { dir, version ->
            dir.dir(os).dir("$DEFAULT_MINICONDA_RELEASE-${version}")
        }
    )

    @get:Input
    internal val pythonEnvName: Property<String> = objects.property<String>().convention(
        pythonVersion.map { ver -> "python-${ver}" }
    )

    @get:Input
    internal val pythonEnvDir: DirectoryProperty = objects.directoryProperty().convention(
        minicondaDir.zip(pythonVersion) { dir, env ->
            dir.dir("envs").dir(env)
        }
    )

    @get:Input
    internal val condaBinDir: DirectoryProperty = objects.directoryProperty().convention(
        minicondaDir.dir("condabin")
    )

    @get:Input
    internal val condaActivatePath: RegularFileProperty = objects.fileProperty().convention(
        if (OperatingSystem.current().isWindows)
            condaBinDir.file("activate.bat")
        else
            minicondaDir.file("bin/activate")
    )

    @get:Input
    internal val condaExec: RegularFileProperty = objects.fileProperty().convention(
        condaBinDir.map { dir ->
            if (OperatingSystem.current().isWindows)
                dir.file("conda.bat")
            else
                dir.file("conda")
        }
    )
}
