package com.example.alloybt.viewpager

import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
//import androidx.navigation.fragment.navArgs
//import com.example.alloybt.BtDevice
//import com.example.alloybt.BtDeviceControlArgs
import com.example.alloybt.R
//import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_page.*

class PageFragment  : Fragment(R.layout.fragment_page) {

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		context?.let { ContextCompat.getColor(it, requireArguments().getInt(KEY_COLOR)) }?.let {
			requireView().setBackgroundColor(
				it
			)
		}
		textHeadView.setText(requireArguments().getInt(KEY_HEAD))
		textView.setText(requireArguments().getInt(KEY_TEXT))
		imageView.setImageResource(requireArguments().getInt(KEY_IMAGE))

		buttonAddBadge.setOnClickListener {
			//ViewPagerFragment?.addBadge()

		}


	}

	override fun onResume() {
		super.onResume()

	}
	companion object {
		private const val KEY_HEAD = "head"
		private const val KEY_TEXT = "text"
		private const val KEY_COLOR = "color"
		private const val KEY_IMAGE = "image"

		fun newInstance(
			@StringRes textHeadRes: Int,
			@StringRes textRes: Int,
			@ColorRes bgColorRes: Int,
			@DrawableRes drawableRes: Int,
		): PageFragment {
			return PageFragment().withArguments {
				putInt(KEY_HEAD, textHeadRes)
				putInt(KEY_TEXT, textRes)
				putInt(KEY_COLOR, bgColorRes)
				putInt(KEY_IMAGE, drawableRes)
			}

		}
	}
}