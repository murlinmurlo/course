package com.my.test


import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.my.test.databinding.ActivityMainBinding
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val deviceAddress = "88:6B:0F:8B:7F:29"
    private val uuid = "00001101-0000-1000-8000-00805f9b34fb"
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private var flag = true

    private var cardiogaphCommunication = CardiogaphCommunication()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cardiograph_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var battaryCharge = cardiogaphCommunication.ReadComponentStatus()
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.battary -> {Toast.makeText(this, "${battaryCharge}%", Toast.LENGTH_SHORT).show()}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.lightYellow)))
            setDisplayHomeAsUpEnabled(true)
            title = "Cardiograph"
        }

        //connection to KR-2
        binding.onClickConnect.setOnClickListener {
            val socket = BluetoothClassicConnect()

            if (flag) {
                BluetoothClassicConnect()
                flag = false
                binding.onClickConnect.text = "Disconnect"
            } else {
                if (socket != null) {
                    BluetoothClassicDisconnect(socket)
                    binding.onClickConnect.text = "Connect"
                }
                flag = true // Переключение состояния обратно
            }
        }

        binding.onClickReadDeviceInformation.setOnClickListener {
            var result = cardiogaphCommunication.ReadDeviceInformation()
            binding.showDeviceInformation.text = result?.toString()
            binding.showDeviceInformation.visibility = View.VISIBLE
        }

        val seekBar = findViewById<SeekBar>(R.id.rateSlider)
        seekBar.max = 1000
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, samplingRate: Int, fromUser: Boolean) {
                var frequency = 0
                when (samplingRate) {
                    0 -> frequency = 1 //250 Hz
                    1 -> frequency = 2 //500 Hz
                    2 -> frequency = 3 //1000 Hz
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                //MonitoringMode(seekBar)
            }
        })

        binding.onClickStartSession.setOnClickListener {
            var frequency = findViewById<SeekBar>(R.id.rateSlider).progress.toChar()
            //var result = cardiogaphCommunication.MonitoringMode(frequency)
            var text = binding.onClickStartSession.text
            when (text) {
                "Start" -> {
                    text = "Stop"
                    "Stop"
                }
                else -> {
                    text = "Start"
                    "Start"
                }
            }
            val layout = findViewById<EcgShowView>(R.id.ecgShow)
            val ecgShowView = EcgShowView(this, null)
            //layout.addView(ecgShowView)  //зачем это тут вообще нужно???

            var dataStr = cardiogaphCommunication.MonitoringMode(frequency) // Заменить на резултат MonitiringMode
            ecgShowView.setData(dataStr.toString())
        }
    }


    private fun BluetoothClassicConnect(): BluetoothSocket? {
        // Checking Bluetooth availability
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            // The device does not support Bluetooth
            Log.d("BluetoothClassic", "Device does not support Bluetooth")
        }

        // Enabling Bluetooth
        if (!bluetoothAdapter?.isEnabled!!) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // Bluetooth enabled
                    Log.d("BluetoothClassic", "Bluetooth enabled")
                } else {
                    // The user declined the request to turn on Bluetooth
                    Log.d("BluetoothClassic", "User declined to enable Bluetooth")
                }
            }.launch(enableBtIntent)
        }

        // Device search
        if (ActivityCompat.checkSelfPermission( // вручную настроить разрешения
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
            return null
        }
        bluetoothAdapter?.startDiscovery()
        Log.d("BluetoothClassic", "Device discovery started")

        // Connecting to the device
        val device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(deviceAddress)
        val socket: BluetoothSocket? = device?.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        Thread {
            socket?.connect()
            inputStream = socket?.inputStream
            outputStream = socket?.outputStream
            Log.d("BluetoothClassic", "Connected to device")
        }.start()

        return socket
    }

    private fun BluetoothClassicDisconnect(socket: BluetoothSocket) {
        // Закрыть Bluetooth-сокет и освободить ресурсы
        inputStream?.close()
        outputStream?.close()
        socket?.close()
        Log.d("BluetoothClassic", "Отключение от устройства")
    }
}


