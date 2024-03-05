package com.iomt.android

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.iomt.android.databinding.ActivityCardiographBinding
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.util.UUID
import com.iomt.android.EcgShowView



class CardiographActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCardiographBinding
    private val deviceAddress = "88:6B:0F:8B:7F:29"
    private val uuid = "00001101-0000-1000-8000-00805f9b34fb"
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private val buffer = ByteArray(1024)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardiographBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()

        binding.onClickReadDeviceInformation.setOnClickListener {
            ReadDeviceInformation()
        }

        val seekBar = findViewById<SeekBar>(R.id.rateSlider)
        seekBar.max = 1000
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, samplingRate: Int, fromUser: Boolean) {}

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                //MonitoringMode(seekBar)
            }
        })

        binding.onClickStartSession.setOnClickListener {

                var text = binding.onClickStartSession.text
                when (text) {
                    "Start session" -> {
                        text = "Stop session"
                        "Stop session"
                    }
                    else -> {
                        text = "Start session"
                        "Start session"
                    }
                }

            //MonitoringMode()
            }
        }


    fun init(){

        // Проверка доступности Bluetooth
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            // Устройство не поддерживает Bluetooth
        }

        // Включение Bluetooth
        if (!bluetoothAdapter?.isEnabled!!) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // Bluetooth включен
                } else {
                    // Пользователь отклонил запрос на включение Bluetooth
                }
            }.launch(enableBtIntent)
        }

        // Поиск устройств
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        bluetoothAdapter?.startDiscovery()

        // Подключение к устройству
        val device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(deviceAddress)
        val socket: BluetoothSocket? = device?.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        Thread {
            socket?.connect()
            inputStream = socket?.inputStream
            outputStream = socket?.outputStream
        }.start()
    }

    private fun ReadDeviceInformation() {
        val buf = ByteBuffer.allocate(4)

        buf.putChar(0.toChar()); // version
        buf.putChar(0.toChar()); // command ReadDeviceInformation

        //outputStream?.write(buf.array())?.toString()

        binding.showDeviceInformation.text = outputStream?.write(buf.array())?.toString()
        binding.showDeviceInformation.visibility = View.VISIBLE

    }

    private fun ReadDeviceStatus() {
        val buf = ByteBuffer.allocate(4)

        buf.putChar(0.toChar()); // version
        buf.putChar(1.toChar()); // command ReadDeviceStatus

        outputStream?.write(buf.array())
    }

    private fun MonitoringMode(samplingRate: SeekBar) {
        val buf = ByteBuffer.allocate(4)
        buf.putChar(0.toChar()); // version
        buf.putChar(4.toChar()); // command MonitoringMode
        buf.putChar(findViewById<SeekBar>(R.id.rateSlider).progress.toChar()); // set 1000 Hz
        outputStream?.write(buf.array())

        val layout = findViewById<EcgShowView>(R.id.ecgShow)
        val ecgShowView = EcgShowView(this, null)
        //layout.addView(ecgShowView)  //зачем это тут вообще нужно???

        val dataStr = "1,2,3,4,5" // Заменить
        ecgShowView.setData(dataStr)
    }

}


