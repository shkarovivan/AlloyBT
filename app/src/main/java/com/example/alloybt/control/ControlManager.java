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

package com.example.alloybt.control;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.UUID;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.livedata.ObservableBleManager;
import no.nordicsemi.android.log.LogContract;
import no.nordicsemi.android.log.LogSession;

public class ControlManager extends ObservableBleManager {
	/** Nordic Blinky Service UUID. */
	public final static UUID LBS_UUID_SERVICE = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
	/** BUTTON characteristic UUID. */
	private final static UUID LBS_UUID_BUTTON_CHAR = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
	/** LED characteristic UUID. */
	private final static UUID LBS_UUID_LED_CHAR = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");

	private final MutableLiveData<Boolean> ledState = new MutableLiveData<>();
	private final MutableLiveData<String> btReceivedData = new MutableLiveData<>();

	private BluetoothGattCharacteristic buttonCharacteristic, ledCharacteristic;
	private boolean supported;

	public ControlManager(@NonNull final Context context) {
		super(context);
	}

	public final LiveData<String> getBtReceivedData() {
		return btReceivedData;
	}

	@NonNull
	@Override
	protected BleManagerGattCallback getGattCallback() {
		return new ControlBleManagerGattCallback();
	}

	@Override
	protected boolean shouldClearCacheWhenDisconnected() {
		return !supported;
	}

	/**
	 * The Button callback will be notified when a notification from Button characteristic
	 * has been received, or its data was read.
	 * <p>
	 * If the data received are valid (single byte equal to 0x00 or 0x01), the
	 * {@link BtReadDataCallback#onDataReceived} will be called.
	 * Otherwise, the {@link BtReadDataCallback#onInvalidDataReceived(BluetoothDevice, Data)}
	 * will be called with the data received.
	 */
	private	final BtReadDataCallback buttonCallback = new BtReadDataCallback() {
		@Override
		public void onDataReceived(@NonNull final BluetoothDevice device,
								   final String data) {
			btReceivedData.setValue(data);
		}

//		@Override
//		public void onInvalidDataReceived(@NonNull final BluetoothDevice device,
//										  @NonNull final Data data) {
//			log(Log.WARN, "Invalid data received: " + data);
//		}
	};

	/**
	 * The LED callback will be notified when the LED state was read or sent to the target device.
	 * <p>
	 * This callback implements both {@link no.nordicsemi.android.ble.callback.DataReceivedCallback}
	 * and {@link no.nordicsemi.android.ble.callback.DataSentCallback} and calls the same
	 * method on success.
	 * <p>
	 * If the data received were invalid, the
	 * {@link BlinkyLedDataCallback#onInvalidDataReceived(BluetoothDevice, Data)} will be
	 * called.
	 */
	private final BlinkyLedDataCallback writeDataCallback = new BlinkyLedDataCallback() {
		@Override
		public void onLedStateChanged(@NonNull final BluetoothDevice device,
									  final boolean on) {
			//ledOn = on;
			log(LogContract.Log.Level.APPLICATION, "LED " + (on ? "ON" : "OFF"));
			ledState.setValue(on);
		}

		@Override
		public void onInvalidDataReceived(@NonNull final BluetoothDevice device,
										  @NonNull final Data data) {
			// Data can only invalid if we read them. We assume the app always sends correct data.
			log(Log.WARN, "Invalid data received: " + data);
		}
	};

	/**
	 * BluetoothGatt callbacks object.
	 */
	private class ControlBleManagerGattCallback extends BleManagerGattCallback {
		@Override
		protected void initialize() {
			setNotificationCallback(buttonCharacteristic).with(buttonCallback);
			readCharacteristic(ledCharacteristic).with(writeDataCallback).enqueue();
			readCharacteristic(buttonCharacteristic).with(buttonCallback).enqueue();
			enableNotifications(buttonCharacteristic).enqueue();
		}

		@Override
		public boolean isRequiredServiceSupported(@NonNull final BluetoothGatt gatt) {
			final BluetoothGattService service = gatt.getService(LBS_UUID_SERVICE);
			if (service != null) {
				buttonCharacteristic = service.getCharacteristic(LBS_UUID_BUTTON_CHAR);
				ledCharacteristic = service.getCharacteristic(LBS_UUID_LED_CHAR);
			}

			boolean writeRequest = false;
			if (ledCharacteristic != null) {
				final int ledProperties = ledCharacteristic.getProperties();
				writeRequest = (ledProperties & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0;
			}

			supported = buttonCharacteristic != null && ledCharacteristic != null && writeRequest;
			return supported;
		}

		@Override
		protected void onServicesInvalidated() {
			buttonCharacteristic = null;
			ledCharacteristic = null;
		}
	}

	/**
	 * Sends a request to the device .
	 *
	 * @param data string.
	 */
	public void setWeldData(final String data) {
		// Are we connected?
		if (ledCharacteristic == null)
			return;
		writeCharacteristic(
				ledCharacteristic,
				btDataSend.setWeldData(data),
				BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
		).with(writeDataCallback).enqueue();
	}
}
