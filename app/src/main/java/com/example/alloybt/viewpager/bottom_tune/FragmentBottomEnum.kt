package com.example.alloybt.viewpager.bottom_tune

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.alloybt.databinding.FragmentDialogEnumBinding
import com.example.alloybt.databinding.FragmentDialogTuneBinding
import com.example.alloybt.viewmodel.ControlViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentBottomEnum : BottomSheetDialogFragment() {

    private val controlViewModel: ControlViewModel by activityViewModels()
    private var _binding: FragmentDialogEnumBinding? = null
    private val binding: FragmentDialogEnumBinding
        get() = _binding!!

    private lateinit var dialog: BottomSheetDialog
    private val args: FragmentBottomTuneArgs by navArgs()

    var newValue = ""
    var address = ""
    var maxIndex  = 0

    private var isActive = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogEnumBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        val tigValue = args.value
        address = tigValue.address
        val index = tigValue.max.toInt()

        (binding.radioGroup.getChildAt(index) as RadioButton).isChecked = true

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            (0..maxIndex).forEach { groupIndex->
                if (group.getChildAt(groupIndex).id == checkedId) { newValue = groupIndex.toString()}
            }
            if (!isActive) {
                isActive = true
                sendValue()
            }
        }

        when (tigValue.address) {
            "1000" -> {
                binding.radio0.text = "AC"
                binding.radio1.text = "AC Pulse"
                binding.radio2.text = "DC"
                binding.radio3.text = "DC _pulse"
                binding.radio4.text = "AC+DC"
                binding.radio5.text = "MMA"
                maxIndex  = 5

            }
            "1001" -> {
                binding.radio0.text = "Осциллятор"
                binding.radio1.text = "LiftTig"
                binding.radio2.visibility = View.GONE
                binding.radio3.visibility = View.GONE
                binding.radio4.visibility = View.GONE
                binding.radio5.visibility = View.GONE
                maxIndex  = 1
            }
            "1002" -> {
                binding.radio0.text = "2T"
                binding.radio1.text = "4T"
                binding.radio2.text = "Точечный"
                binding.radio3.text = "Режим повтора"
                binding.radio4.visibility = View.GONE
                binding.radio5.visibility = View.GONE
                maxIndex  = 3
            }

            "1003" -> {
                binding.radio0.text = "Треугольник"
                binding.radio1.text = "Синус"
                binding.radio2.text = "Меандр"
                binding.radio3.text = "Трапеция"
                binding.radio4.visibility = View.GONE
                binding.radio5.visibility = View.GONE
                maxIndex  = 3
            }
        }
    }

    private fun getJsonString(value: String, address: String): String {
        return "{\"Write\":{\"$address\":$value}}"
    }

    private fun sendWriteValueRequest(value: String, address: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val jsonString = getJsonString(value, address)
                controlViewModel.setWeldData(jsonString)
                Log.d("writeData", jsonString)
            } catch (e: Exception) {
                toast("movie to JSON error = ${e.message}")
            }
        }
    }

    private fun sendValue() {
        lifecycleScope.launch(Dispatchers.IO) {
            while (isActive) {
                sendWriteValueRequest(newValue, address)
                delay(200)
            }
        }
    }

    private fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}