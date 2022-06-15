package com.pswidersk.gradle.python

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.internal.file.FileFactory
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

abstract class PythonPluginExtension @Inject constructor(
    providerFactory: ProviderFactory,
    project: Project,
    objects: ObjectFactory,
    fileFactory: FileFactory
) {

    val pythonVersion: Property<String> = objects.property<String>().convention(DEFAULT_PYTHON_VERSION)

    val minicondaVersion: Property<String> = objects.property<String>().convention(DEFAULT_MINICONDA_VERSION)

    val minicondaRepoUrl: Property<String> = objects.property<String>().convention(DEFAULT_MINICONDA_REPO_URL)

    val minicondaRepoUsername: Property<String> = objects.property<String>().convention("")

    val minicondaRepoPassword: Property<String> = objects.property<String>().convention("")

    val minicondaRepoHeaders: MapProperty<String, String> =
        objects.mapProperty(String::class.java, String::class.java)

    val installDir: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider {
            fileFactory.dir(project.rootDir).dir(GRADLE_FILES_DIR).dir(PYTHON_ENVS_DIR)
        }
    )

    internal val minicondaDir: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider {
            installDir.get()
                .dir(os)
                .dir("$DEFAULT_MINICONDA_RELEASE-${minicondaVersion.get()}")
        }
    )

    internal val pythonEnvName: Property<String> = objects.property<String>().convention(
        providerFactory.provider { "python-${pythonVersion.get()}" }
    )

    internal val pythonEnvDir: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider { minicondaDir.get().dir("envs").dir(pythonEnvName.get()) }
    )

    internal val condaBinDir: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider { minicondaDir.get().dir("condabin") }
    )

    internal val condaActivatePath: RegularFileProperty = objects.fileProperty().convention(
        providerFactory.provider {
            if (OperatingSystem.current().isWindows)
                condaBinDir.get().file("activate.bat")
            else
                minicondaDir.get().dir("bin").file("activate")
        }
    )

    internal val condaExec: RegularFileProperty = objects.fileProperty().convention(
        providerFactory.provider {
            if (OperatingSystem.current().isWindows)
                this.condaBinDir.get().file("conda.bat")
            else
                this.condaBinDir.get().file("conda")
        }
    )
}