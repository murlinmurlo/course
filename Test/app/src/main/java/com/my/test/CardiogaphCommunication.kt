package com.my.test

import android.view.View
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer

class CardiogaphCommunication {
    private val deviceAddress = "88:6B:0F:8B:7F:29"
    private val uuid = "00001101-0000-1000-8000-00805f9b34fb"
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private val buffer = ByteArray(1024)
    fun ReadDeviceInformation(): String {
        val buf = ByteBuffer.allocate(4)

        buf.putChar(0.toChar()); // version
        buf.putChar(0.toChar()); // command ReadDeviceInformation

        outputStream?.write(buf.array())

        val response = ByteArray(1024)
        val bytesRead = inputStream?.read(response)
        val result = response.copyOfRange(0, bytesRead ?: 0).toString(Charsets.UTF_8)

        return result
    }
    fun ReadDeviceStatus(): String {
        val buf = ByteBuffer.allocate(4)

        buf.putChar(0.toChar()); // version
        buf.putChar(1.toChar()); // command ReadDeviceStatus

        outputStream?.write(buf.array())

        val response = ByteArray(1024)
        val bytesRead = inputStream?.read(response)
        val result = response.copyOfRange(0, bytesRead ?: 0).toString(Charsets.UTF_8)

        return result
    }

    fun ReadComponentStatus(): String {
        val buf = ByteBuffer.allocate(4)

        buf.putChar(0.toChar()); // version
        buf.putChar(3.toChar()); // command ReadComponentStatus

        outputStream?.write(buf.array())

        val response = ByteArray(1024)
        val bytesRead = inputStream?.read(response)
        val result = response.copyOfRange(0, bytesRead ?: 0).toString(Charsets.UTF_8)

        return result
    }

    fun PoverOff(): String {
        val buf = ByteBuffer.allocate(4)

        buf.putChar(0.toChar()); // version
        buf.putChar(10.toChar()); // command PoverOff

        outputStream?.write(buf.array())

        val response = ByteArray(1024)
        val bytesRead = inputStream?.read(response)
        val result = response.copyOfRange(0, bytesRead ?: 0).toString(Charsets.UTF_8)

        return result
    }
}