/*
 * Copyright (c) 2018, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.example.alloybt.viewmodel;

import android.app.Application;
import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.alloybt.control.ControlManager;
import com.example.alloybt.viewpager.device_control.ControlParam;

import java.util.List;

import no.nordicsemi.android.ble.ConnectRequest;
import no.nordicsemi.android.ble.livedata.state.ConnectionState;
import no.nordicsemi.android.log.LogSession;
import no.nordicsemi.android.log.Logger;

public class ControlViewModel extends AndroidViewModel {
	private final ControlManager controlManager;
	private BluetoothDevice btDevice;
	@Nullable
	private ConnectRequest connectRequest;

	private MutableLiveData<MonitorMode> monitorMode;

	public MutableLiveData<MonitorMode> getMonitorMode(){
		if (monitorMode == null){
			monitorMode = new MutableLiveData<>();
		}
		return monitorMode;
	}

	public ControlViewModel(@NonNull final Application application) {
		super(application);
		// Initialize the manager.
		controlManager = new ControlManager(getApplication());
	}

	public LiveData<ConnectionState> getConnectionState() {
		return controlManager.state;
	}

	public LiveData<String> getDataFromBtDevice() {
		return controlManager.getBtReceivedData();
	}

	/**
	 * Connect to the given peripheral.
	 *
	 * @param device the target device.
	 */
	public void connect(@NonNull final BluetoothDevice device) {
		// Prevent from calling again when called again (screen orientation changed).
		btDevice = device;
		reconnect();
	}

	/**
	 * Reconnects to previously connected device.
	 * If this device was not supported, its services were cleared on disconnection, so
	 * reconnection may help.
	 */
	public void reconnect() {
		connectRequest = controlManager.connect(btDevice)
				.retry(3, 100)
				.useAutoConnect(false)
				.then(d -> connectRequest = null);
		connectRequest.enqueue();
	}

	/**
	 * Disconnect from peripheral.
	 */
	public void disconnect() {
		btDevice = null;
		if (connectRequest != null) {
			connectRequest.cancelPendingConnection();
		} else if (controlManager.isConnected()) {
			controlManager.disconnect().enqueue();
		}
	}

	/**
	 * Sends a command to turn ON or OFF the LED on the nRF5 DK.
	 *
	 * @param data true to turn the LED on, false to turn it OFF.
	 */
	public void setWeldData(final String data) {
		controlManager.setWeldData(data);
	}

	@Override
	protected void onCleared() {
		super.onCleared();
		disconnect();
	}
}
