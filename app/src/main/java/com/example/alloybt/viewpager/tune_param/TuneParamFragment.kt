package com.example.alloybt.viewpager.tune_param

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.alloybt.R
import com.example.alloybt.databinding.FragmentTuneParamBinding
import com.example.alloybt.viewpager.device_control.ControlParam

class TuneParamFragment : Fragment(R.layout.fragment_tune_param) {

    private var _binding: FragmentTuneParamBinding? = null
    private val binding get() = _binding!!

    private val args: TuneParamFragmentArgs by navArgs()
    lateinit var controlParam: ControlParam

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTuneParamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controlParam = args.controlParam
        when (controlParam.type) {
                 "Float" -> {
                     binding.paramValue.text = ""
                     binding.croller.progress = controlParam.value.toInt()
                     binding.croller.min = (controlParam.min.toFloat() * 10).toInt()
                     binding.croller.max = (controlParam.max.toFloat() * 10).toInt()
                     binding.paramValue.text = (controlParam.value.toFloat() * 10).toString()
                     binding.paramDescr.text = controlParam.description

                     binding.croller.setOnProgressChangedListener { value ->
                         binding.paramValue.text = (value / 10f).toString()
                     }
                 }
        }
        binding.confirmButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}