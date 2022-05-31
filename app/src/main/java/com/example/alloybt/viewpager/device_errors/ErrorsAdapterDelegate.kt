package com.skillbox.multithreading.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.R
import com.example.alloybt.adapter.inflate
import com.example.alloybt.json_data.TigError
import com.example.alloybt.viewpager.device_errors.DeviceError
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_device_error.*


class ErrorsAdapterDelegate(
	private val onItemClick: (position: Int) -> Unit,
) : AbsListItemAdapterDelegate<TigError, TigError, ErrorsAdapterDelegate.ErrorHolder>() {


	override fun isForViewType(
		item: TigError,
		items: MutableList<TigError>,
		position: Int,
	): Boolean {
		return true
	}

	override fun onCreateViewHolder(parent: ViewGroup): ErrorHolder {
		return ErrorHolder(parent.inflate(R.layout.item_device_error), onItemClick)
	}

	override fun onBindViewHolder(item: TigError, holder: ErrorHolder, payloads: MutableList<Any>) {
		holder.bind(item)
	}

	class ErrorHolder(
		override val containerView: View,
		onItemClick: (position: Int) -> Unit,
	) : RecyclerView.ViewHolder(containerView), LayoutContainer {

		init {
			containerView.setOnClickListener {
				onItemClick(bindingAdapterPosition)
			}
		}

		@SuppressLint("SetTextI18n")
		fun bind(error: TigError) {
			errorTimeTextView.text =
				when (error.code) {
					else -> ""
				}

			errorNumTextView.text = (error.num +1).toString()

			errorDescrTextView.text =
				when (error.code) {
					15 -> "перегрев аппарата"
					16 -> ""
					17 -> ""
					18 -> ""
					19 -> ""
					else -> "Не определено"

				}

			errorTimeTextView.text = error.time.toString()
		}
	}

}

