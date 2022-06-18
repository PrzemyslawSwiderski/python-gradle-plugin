import com.pswidersk.gradle.python.VenvTask

plugins {
    id("com.pswidersk.python-plugin")
}

pythonPlugin {
    pythonVersion.set("3.7.0")
    condaInstaller.set("Anaconda3")
    condaVersion.set("2022.05")
    condaRepoUrl.set("https://repo.anaconda.com/archive/")
}

tasks {

    val condaInstall by registering(VenvTask::class) {
        venvExec = "conda"
        val dependencies = listOf(
            "matplotlib=3.5.1",
            "scikit-learn=1.0.2"
        )
        args = listOf("install") + dependencies
    }

    register<VenvTask>("runPlotDatasetScript") {
        args = listOf("plot_iris_dataset.py")

        dependsOn(condaInstall)
    }
}