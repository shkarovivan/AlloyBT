package com.example.alloybt.viewpager.bottom_tune

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.alloybt.databinding.FragmentDialogFastProgramsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import timber.log.Timber.d

class FragmentFastPrograms: BottomSheetDialogFragment(){

 //   private val controlViewModel: ControlViewModel by activityViewModels()
    private var _binding: FragmentDialogFastProgramsBinding? = null
    private val binding: FragmentDialogFastProgramsBinding
        get() = _binding!!

    private lateinit var dialog: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogFastProgramsBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        Log.d("FastDialog", "onCreateDialog")
        return dialog
    }

       override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
           Log.d("FastDialog", "onViewCreated")
           binding.button1.setOnClickListener {
               dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
           }
         //  dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}