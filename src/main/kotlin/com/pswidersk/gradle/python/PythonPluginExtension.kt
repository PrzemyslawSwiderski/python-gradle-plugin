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
import org.gradle.kotlin.dsl.mapProperty
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

abstract class PythonPluginExtension @Inject constructor(
    providerFactory: ProviderFactory,
    project: Project,
    objects: ObjectFactory,
    fileFactory: FileFactory
) {

    val pythonVersion: Property<String> = objects.property<String>().convention(DEFAULT_PYTHON_VERSION)

    val condaVersion: Property<String> = objects.property<String>().convention(DEFAULT_CONDA_VERSION)

    val condaInstaller: Property<String> = objects.property<String>().convention(DEFAULT_CONDA_INSTALLER)

    val condaRepoUrl: Property<String> = objects.property<String>().convention(DEFAULT_CONDA_REPO_URL)

    val condaRepoUsername: Property<String> = objects.property()

    val condaRepoPassword: Property<String> = objects.property()

    val condaRepoHeaders: MapProperty<String, String> = objects.mapProperty()

    val installDir: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider {
            fileFactory.dir(project.rootDir).dir(GRADLE_FILES_DIR).dir(PYTHON_ENVS_DIR)
        }
    )

    val systemArch: Property<String> = objects.property<String>().convention(arch)

    internal val condaDir: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider {
            installDir.get()
                .dir(os)
                .dir("${condaInstaller.get()}-${condaVersion.get()}")
        }
    )

    internal val pythonEnvName: Property<String> = objects.property<String>().convention(
        providerFactory.provider { "python-${pythonVersion.get()}" }
    )

    internal val pythonEnvDir: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider { condaDir.get().dir("envs").dir(pythonEnvName.get()) }
    )

    internal val condaBinDir: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider { condaDir.get().dir("condabin") }
    )

    internal val condaActivatePath: RegularFileProperty = objects.fileProperty().convention(
        providerFactory.provider {
            if (OperatingSystem.current().isWindows)
                condaBinDir.get().file("activate.bat")
            else
                condaDir.get().dir("bin").file("activate")
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