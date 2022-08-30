package com.pswidersk.gradle.python

import org.apache.commons.io.FileUtils.byteCountToDisplaySize

class ProgressLogger(
    private val consumer: (progressMsg: String) -> Unit = {},
    private val step: Int = 10
) {

    private val stepsDeque = ArrayDeque((0..100 step this.step).toList())

    fun invoke(bytesSent: Long, bytesTotal: Long) {
        val currentPercent = calcPercent(bytesSent, bytesTotal)

        if (stepsDeque.isNotEmpty() && currentPercent >= stepsDeque.first()) {
            consumer.invoke(
                "Bytes processed: ${byteCountToDisplaySize(bytesSent)}, Total: ${
                    byteCountToDisplaySize(
                        bytesTotal
                    )
                }, Progress: $currentPercent%"
            )
            stepsDeque.removeFirstOrNull()
        }
    }

    private fun calcPercent(bytesSent: Long, bytesTotal: Long): Int {
        return ((bytesSent.toDouble() / bytesTotal) * 100).toInt()
    }
}
