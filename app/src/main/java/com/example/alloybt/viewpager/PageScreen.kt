package com.example.alloybt.viewpager

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class PageScreen(
	@StringRes val textHeadRes: Int,
	@StringRes val textRes: Int,
	@ColorRes val bgColorRes: Int,
	@DrawableRes val drawableRes: Int,
)
