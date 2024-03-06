package com.iomt.android.bluetooth
import com.iomt.android.configs.DeviceConfig

/**
 * @property device [BluetoothDevice]
 * @property config [DeviceConfig] selected for [device]
 */
data class BluetoothDeviceWithConfig(
    val device: android.bluetooth.BluetoothDevice,
    val config: DeviceConfig,
)
