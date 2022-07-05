package com.example.alloybt

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(){
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		startLockTask()
	}

	override fun startLockTask() {
		val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
		if (am.lockTaskModeState == ActivityManager.LOCK_TASK_MODE_NONE) {
			super.startLockTask()
		}
	}

}