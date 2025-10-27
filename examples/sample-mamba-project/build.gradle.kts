import com.pswidersk.gradle.python.VenvTask

plugins {
    id("com.pswidersk.python-plugin")
}


tasks {

    val condaInstall by registering(VenvTask::class) {
        venvExec = "mamba"
        val dependencies = listOf(
            "matplotlib=3.10.7",
            "scikit-learn=1.7.2"
        )
        args = listOf("install") + dependencies
    }

    register<VenvTask>("runPlotDatasetScript") {
        args = listOf("plot_iris_dataset.py")

        dependsOn(condaInstall)
    }
}
