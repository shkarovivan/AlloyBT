package com.example.alloybt.viewpager.device_program

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.databinding.ItemDeviceProgramBinding
import com.example.alloybt.json_data.TigProgram
import com.example.alloybt.json_data.tigButtonMode
import com.example.alloybt.json_data.tigProgramMode
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class DeviceProgramAdapterDelegate(
    private val onSaveClick: (position: Int) -> Unit,
    private val onLoadClick: (position: Int) -> Unit,
) : AbsListItemAdapterDelegate<TigProgram, TigProgram, DeviceProgramAdapterDelegate.DeviceProgramHolder>() {


    override fun isForViewType(
        item: TigProgram,
        items: MutableList<TigProgram>,
        position: Int
    ): Boolean {
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup): DeviceProgramHolder {
        val itemBinding =
            ItemDeviceProgramBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeviceProgramHolder(itemBinding, onSaveClick, onLoadClick)
    }

    override fun onBindViewHolder(
        item: TigProgram,
        holder: DeviceProgramHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class DeviceProgramHolder(
        private val binding: ItemDeviceProgramBinding,
        onSaveClick: (position: Int) -> Unit,
        onLoadClick: (position: Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageDownload.setOnClickListener {
                onSaveClick(bindingAdapterPosition)
            }
            binding.imageUpload.setOnClickListener {
                onLoadClick(bindingAdapterPosition)
            }
        }


        fun bind(program: TigProgram) {
            binding.programNumberTextView.text = (program.number + 1).toString()
            if (program.mode == 255) {
                binding.programDescriptionTextView.text = "Пусто"
            } else {
                binding.programDescriptionTextView.text =
                    " ${tigProgramMode[program.mode]}\n${program.current} A\n${tigButtonMode[program.buttonMode]}"
            }
        }
    }
}

