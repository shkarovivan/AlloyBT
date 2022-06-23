package com.skillbox.multithreading.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.R
import com.example.alloybt.utils.inflate
import com.example.alloybt.json_data.TigError
import com.example.alloybt.json_data.tigErrorsDescriptions
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
			errorImage.setImageLevel(error.level)

			errorDescrTextView.text = tigErrorsDescriptions[error.code]

			var dateString = ""
			//errorTimeTextView.text = error.time.toString()
			var timestamp = error.time
			val seconds = timestamp % 64
			timestamp =	timestamp shr 6
			val minutes = timestamp % 64
			timestamp = timestamp shr 6
			val hours = timestamp % 32
			timestamp = timestamp shr 5
			val date = timestamp % 32
			timestamp = timestamp shr 5
			val month = timestamp % 16
			val year = timestamp shr 4
			dateString +=
				if (year<10) {
				"200$year"
			} else	if (year<100) {
				"20$year"
			} else  {
				"2$month"
			}
			dateString += if (month<10) {
				".0$month"
			} else {
				".$month"
			}
			dateString += if (date<10) {
				".0$date"
			} else {
				".$date"
			}
			dateString += if (hours<10) {
				"  0$hours"
			} else {
				"  $hours"
			}
			dateString += if (minutes<10) {
				":0$minutes"
			} else {
				":$minutes"
			}
			dateString += if (seconds<10) {
				":0$seconds"
			} else {
				":$seconds"
			}

			errorTimeTextView.text = dateString
				//"$year.$month.$date $hours:$minutes:$seconds"
		}
	}

}

