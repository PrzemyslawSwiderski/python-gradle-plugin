package com.pswidersk.gradle.python

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows

class ProgressLoggerTest {

    @Test
    fun `it should log progress properly for step = 25`() {
        val expectedMessages = listOf(
            "Bytes processed: 0 bytes, Total: 2 MB, Progress: 0%",
            "Bytes processed: 512 KB, Total: 2 MB, Progress: 25%",
            "Bytes processed: 1 MB, Total: 2 MB, Progress: 50%",
            "Bytes processed: 1 MB, Total: 2 MB, Progress: 75%",
            "Bytes processed: 2 MB, Total: 2 MB, Progress: 100%"
        )
        val resultMessages = ArrayDeque<String>()

        val inputStep = 25
        val totalBytes = 2_097_152
        val progressLogger = ProgressLogger(
            consumer = { msg ->
                resultMessages.addLast(msg)
            },
            step = inputStep
        )

        for (i in 0..totalBytes) {
            progressLogger.invoke(i.toLong(), totalBytes.toLong())
        }
        assertThat(resultMessages).containsSequence(expectedMessages)
    }

    @Test
    fun `it should throw exception`() = assertAll(listOf(-1, 0).map { illegalInputStep ->
        {
            val totalBytes = 200

            assertThrows<IllegalArgumentException> {
                val progressLogger = ProgressLogger(
                    step = illegalInputStep
                )
                for (i in 1..totalBytes) {
                    progressLogger.invoke(i.toLong(), totalBytes.toLong())
                }
            }
        }
    })
}