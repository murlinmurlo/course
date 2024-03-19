package com.my.test


import android.util.Log
import com.my.readdeviceinformation.RFC1055
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer

class CardiogaphCommunication {
    private val deviceAddress = "88:6B:0F:8B:7F:29"
    private val uuid = "00001101-0000-1000-8000-00805f9b34fb"
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private val buffer = ByteArray(1024)
    private var code = RFC1055


    private fun sendCommand(command: List<Byte>): String {
        val buf = ByteBuffer.allocate(4)
        //buf.putChar(0.toChar()) // version
        buf.put(RFC1055.encode(command)) // command

        outputStream?.write(buf.array())

        val response = ByteArray(1024)
        val bytesRead = inputStream?.read(response)
        val result = response.copyOfRange(0, bytesRead ?: 0).toString(Charsets.UTF_8)

        return result
    }

    fun ReadDeviceInformation(): ByteArray? {
        val command = listOf<Byte>(
            0x00.toByte(),
            0x00.toByte(),
            0x02.toByte(),
        )
        val encodedCommand = RFC1055.encode(command)

        val buf = ByteBuffer.allocate(encodedCommand.size)
        buf.put(encodedCommand)
        buf.flip()
        outputStream?.write(buf.array())

        val response = ByteArray(1024)
        val bytesRead = inputStream?.read(response)
        val (decodedData, bytesProcessed) = RFC1055.decode(response.toList(), response.size)

        return decodedData
    }

    fun ReadDeviceStatus(): ByteArray? {
        val command = listOf<Byte>(
            0x00.toByte(),
            0x01.toByte(),
            0x03.toByte(),
        )
        val encodedCommand = RFC1055.encode(command)

        val buf = ByteBuffer.allocate(encodedCommand.size)
        buf.put(encodedCommand)
        buf.flip()
        outputStream?.write(buf.array())

        val response = ByteArray(1024)
        val bytesRead = inputStream?.read(response)
        val (decodedData, bytesProcessed) = RFC1055.decode(response.toList(), response.size)

        return decodedData
    }

    fun ReadComponentStatus(): ByteArray?{
        val command = listOf<Byte>(
            0x00.toByte(),
            0x02.toByte(),
            0x04.toByte(),
        )
        val encodedCommand = RFC1055.encode(command)

        val buf = ByteBuffer.allocate(encodedCommand.size)
        buf.put(encodedCommand)
        buf.flip()
        outputStream?.write(buf.array())

        val response = ByteArray(1024)
        val bytesRead = inputStream?.read(response)
        val (decodedData, bytesProcessed) = RFC1055.decode(response.toList(), response.size)

        return decodedData
    }

    fun PowerOff() {
        val command = listOf<Byte>(
            0x00.toByte(),
            0x0A.toByte(),
            0x0C.toByte(),
        )
        val encodedCommand = RFC1055.encode(command)

        val buf = ByteBuffer.allocate(encodedCommand.size)
        buf.put(encodedCommand)
        buf.flip()
        outputStream?.write(buf.array())
    }

    fun MonitoringMode(frequency: Char): ByteArray?{
        val command = listOf<Byte>(
            0x00.toByte(),
            0x04.toByte(),
            0x03.toByte(),
            0x0a.toByte()
        )
        val encodedCommand = RFC1055.encode(command)

        val buf = ByteBuffer.allocate(encodedCommand.size)
        buf.put(encodedCommand)
        buf.flip()
        outputStream?.write(buf.array())

        val response = ByteArray(1024)
        val bytesRead = inputStream?.read(response)
        val (decodedData, bytesProcessed) = RFC1055.decode(response.toList(), response.size)

        return decodedData
    }
}