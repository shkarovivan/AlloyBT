package com.example.alloybt.utils

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.alloybt.R
import com.example.alloybt.json_data.Password
import com.example.alloybt.viewpager.device_monitor.BtDeviceMonitorFragment
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlin.contracts.contract


fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
	return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}


@RequiresApi(Build.VERSION_CODES.P)
fun showAlertDialog(context: Context, listener: (String) ->Unit) {

	val edittext = EditText(context)
	edittext.hint = context.getString(R.string.password_edit_text_hint_text)
	edittext.maxLines = 1
	edittext.setBackgroundColor(context.getColor(R.color.light_grey))
	edittext.setTextColor(context.getColor(R.color.white))
	edittext.outlineAmbientShadowColor = context.getColor(R.color.dark_yellow)
	edittext.highlightColor = context.getColor(R.color.dark_yellow)
	edittext.outlineSpotShadowColor = context.getColor(R.color.dark_yellow)
	edittext.setLinkTextColor(context.getColor(R.color.dark_yellow))
	edittext.setHintTextColor(context.getColor(R.color.grey))
	val layout = FrameLayout(context)
//set padding in parent layout
	layout.setPaddingRelative(45, 15, 45, 0)
	layout.addView(edittext)
	AlertDialog.Builder(context,R.style.AlertDialogCustom)
		.setTitle(R.string.password_dialog_title)
	.setView(layout)
	.setPositiveButton(R.string.password_dialog_pos_button_text)
	{ dialog, which ->
		run {
			Password.password = edittext.text.toString()
			listener(Password.password!!)
			dialog.dismiss()
		}
	}
	.setNegativeButton(R.string.password_dialog_neg_button_text)
	{ dialog, which ->
		run {
			dialog.dismiss()
		}
	}
	.show()
}
