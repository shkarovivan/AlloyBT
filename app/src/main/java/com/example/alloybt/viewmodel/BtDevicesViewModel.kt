package com.example.alloybt.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alloybt.BtDevice
import com.example.alloybt.SingleLiveEvent

class BtDevicesViewModel(application: Application): AndroidViewModel(application) {

	private val repository = BtDevicesRepository()
	private val btDevicesLiveData = MutableLiveData(repository.initBtDevicesList())

	val btDevicesList: LiveData<List<BtDevice>>
		get() = btDevicesLiveData

	private var showLiveDataToast = SingleLiveEvent<Unit>()

	val showToast: LiveData<Unit>
		get() = showLiveDataToast

	private var btDevices: List<BtDevice> = repository.initBtDevicesList()

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
}