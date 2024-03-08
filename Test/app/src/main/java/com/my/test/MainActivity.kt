package com.my.test


import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import java.nio.ByteBuffer
import java.util.UUID



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val deviceAddress = "88:6B:0F:8B:7F:29"
    private val uuid = "00001101-0000-1000-8000-00805f9b34fb"
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private val buffer = ByteArray(1024)
    private var cardiogaphCommunication = CardiogaphCommunication()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cardiograph_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.battary -> {Toast.makeText(this, "69%", Toast.LENGTH_SHORT).show()}
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


        BluetoothClassicConnecion()

        binding.onClickReadDeviceInformation.setOnClickListener {
            var result = cardiogaphCommunication.ReadDeviceInformation()
            binding.showDeviceInformation.text = result?.toString()
            binding.showDeviceInformation.visibility = View.VISIBLE
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
            var frequency = findViewById<SeekBar>(R.id.rateSlider).progress.toChar()
            var result = cardiogaphCommunication.MonitoringMode(frequency)
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
            val layout = findViewById<EcgShowView>(R.id.ecgShow)
            val ecgShowView = EcgShowView(this, null)
            //layout.addView(ecgShowView)  //зачем это тут вообще нужно???

            val dataStr = "1,2,3,4,5" // Заменить
            ecgShowView.setData(dataStr)
        }
    }


    fun BluetoothClassicConnecion(){
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
}


