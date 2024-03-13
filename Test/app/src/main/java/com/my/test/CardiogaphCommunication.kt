package com.my.test


import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer

class CardiogaphCommunication {
    private val deviceAddress = "88:6B:0F:8B:7F:29"
    private val uuid = "00001101-0000-1000-8000-00805f9b34fb"
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private val buffer = ByteArray(1024)

    private fun sendCommand(command: Char): String {
        val buf = ByteBuffer.allocate(4)
        buf.putChar(0.toChar()) // version
        buf.putChar(command) // command

        outputStream?.write(buf.array())

        val response = ByteArray(1024)
        val bytesRead = inputStream?.read(response)
        val result = response.copyOfRange(0, bytesRead ?: 0).toString(Charsets.UTF_8)

        return result
    }

    fun ReadDeviceInformation(): String {
        return sendCommand(0.toChar())
    }

    fun ReadDeviceStatus(): String {
        return sendCommand(1.toChar())
    }

    fun ReadComponentStatus(): String {
        return sendCommand(3.toChar())
    }

    fun PoverOff(): String {
        return sendCommand(10.toChar())
    }

    fun MonitoringMode(frequency: Char): String {
        val buf = ByteBuffer.allocate(4)
        buf.putChar(0.toChar()) // version
        buf.putChar(4.toChar()) // command MonitoringMode
        buf.putChar(frequency.toChar()) // set frequency
        outputStream?.write(buf.array())

        val response = ByteArray(1024)
        val bytesRead = inputStream?.read(response)
        val result = response.copyOfRange(0, bytesRead ?: 0).toString(Charsets.UTF_8)

        return result
    }
}
