package com.example.alloybt.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.*
import android.content.pm.PackageManager
import android.os.ParcelUuid
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.util.containsValue
import androidx.core.util.forEach
import androidx.lifecycle.*
import com.example.alloybt.BluetoothAdapterProvider
import com.example.alloybt.BtDevice
import com.example.alloybt.SingleLiveEvent
import com.example.alloybt.viewpager.device_control.ControlParam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.nio.charset.StandardCharsets

class BtDevicesViewModel(adapterProvider: BluetoothAdapterProvider) : ViewModel() {

    private val repository = BtDevicesRepository()
    private val btDevicesLiveData = MutableLiveData(repository.initBtDevicesList())


    private val adapter = adapterProvider.getAdapter()
    private var scanner: BluetoothLeScanner? = null
    private var callback: BleScanCallback? = null

    private val settings: ScanSettings = buildSettings()
    private val filters: List<ScanFilter> = buildFilter()

    private val foundDevices = HashMap<String, BluetoothDevice>()

    val btDevicesList: LiveData<List<BtDevice>>
        get() = btDevicesLiveData

    private var showLiveDataToast = SingleLiveEvent<Unit>()

    val showToast: LiveData<Unit>
        get() = showLiveDataToast

    private var btDevices: List<BtDevice> = repository.initBtDevicesList()

    private val s: String = "ALLOY"
    val b = s.toByteArray(StandardCharsets.US_ASCII);

//	fun addPhone() {
//		val newPhone = repository.createPhone()
//		val updatedList = listOf(newPhone) + phoneLiveData.value.orEmpty()
//		phoneLiveData.postValue(updatedList)
//	}
//
//	fun deletePhone(position: Int) {
//		phoneLiveData.postValue(repository.deletePhone(phoneLiveData.value.orEmpty(), position))
//		showLiveDataToast.postValue(Unit)
//	}

    fun isListEmpty(): Boolean = btDevices.isEmpty()

    fun refreshList() {
        btDevicesLiveData.postValue(repository.initBtDevicesList())
    }

    private fun buildSettings() =
        ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

    private fun buildFilter() =
        listOf(
            ScanFilter.Builder()
                //.setDeviceAddress("E1:9F:FA:73:6D:24")
                //.setServiceUuid(FILTER_UUID)
                //.setServiceUuid(FILTER_UUID)
                .build()
        )

    fun startScan() {
        if (callback == null) {
            callback = BleScanCallback()
            scanner = adapter.bluetoothLeScanner
            Log.e("BluetoothScanner", "Start scan.")
            scanner?.startScan(null, settings, callback)
        }
    }

    fun stopScan() {
        if (callback != null) {
            scanner?.stopScan(callback)
            scanner = null
            callback = null
        }
    }

    fun addBtDevice(
        model: String,
        macAddress: String,
        number: String,
        btSignalLevel: Int,
        btDevice: BluetoothDevice
    ) {
        val newBtDevice = repository.addBtDevice(model, macAddress, number, btSignalLevel, btDevice)
        var sameBtDevice = false
        val oldList = btDevicesLiveData.value.orEmpty()
        oldList.forEach {
            if (it.macAddress == macAddress) sameBtDevice = true
        }

        if (!sameBtDevice) {
            val updatedList = listOf(newBtDevice) + btDevicesLiveData.value.orEmpty()
            btDevicesLiveData.postValue(updatedList)
        }
    }

    inner class BleScanCallback : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            foundDevices[result.device.address] = result.device
            Log.e("BluetoothScanner", "scan.")
            var btDeviceName: String = result.device.name ?: "Unknown"//result.scanRecord.toString()//
            var btDeviceManuf: String = result.scanRecord?.manufacturerSpecificData?.get(2573)?.toString(Charsets.UTF_8) ?: "null"
            if (btDeviceManuf.contains("ALLOY")) {
                val startIndex = btDeviceName.indexOf('N', 0, false)
                val endIndex = btDeviceName.length
                val number = btDeviceName.substring(startIndex + 1, endIndex - 1)
              //  btDeviceName = btDeviceName.substring(0, startIndex)
                addBtDevice(btDeviceName, result.device.address,"" /*number*/, result.rssi, result.device)
            }
 //           else(
 //               addBtDevice(
//                btDeviceName,
//                result.device.address,
//                "no number",
//                result.rssi,
//                result.device
 //           )


            Log.e("BluetoothScanner", "Scan result:  ${result.rssi}  ")
            //	_devices.postValue(foundDevices.values.toList())
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            results.forEach { result ->
                foundDevices[result.device.address] = result.device
            }
            Log.e("BluetoothScanner", "Scan result:  ")
            //	_devices.postValue(foundDevices.values.toList())
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("BluetoothScanner", "onScanFailed:  scan error $errorCode")
        }
    }

    companion object {
        val FILTER_UUID: ParcelUuid =
            ParcelUuid.fromString("0000fff0-0000-1000-8000-00805f9b34fb")//"6f59f19e-2f39-49de-8525-5d2045f4d999")
    }
}


class DeviceViewModelFactory(
    private val adapterProvider: BluetoothAdapterProvider,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BtDevicesViewModel::class.java)) {
            return BtDevicesViewModel(adapterProvider) as T
        }
        throw IllegalArgumentException("View Model not found")
    }
}