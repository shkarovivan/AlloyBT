package com.example.alloybt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.alloybt.viewpager.AddBadge
import com.example.alloybt.viewpager.device_monitor.BtDeviceMonitorFragment

class MainActivity : AppCompatActivity(){
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

}