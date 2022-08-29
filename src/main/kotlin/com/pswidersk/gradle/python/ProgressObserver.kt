package com.pswidersk.gradle.python

import org.apache.commons.io.input.ObservableInputStream
import java.net.URLConnection

class ProgressObserver(
    private val progressLogger: ProgressLogger,
    private val connection: URLConnection,
    private val bufferSize: Int = DEFAULT_BUFFER_SIZE
) : ObservableInputStream.Observer() {
    private var chunksRead = 0L

    override fun data(buffer: ByteArray, offset: Int, length: Int) {
        progressLogger.invoke(chunksRead * bufferSize, connection.contentLengthLong)
        chunksRead++
    }
}