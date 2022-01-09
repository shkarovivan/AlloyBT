package com.example.alloybt

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alloybt.adapter.BtDevicesAdapter
import com.example.alloybt.viewmodel.BtDevicesViewModel
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_search_devices.*

class SearchDevicesFragment : Fragment(R.layout.fragment_search_devices) {

	private val btDevicesListViewModel: BtDevicesViewModel by viewModels()

	private var btDevicesAdapter: BtDevicesAdapter? = null

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initList()
		searchFAB.setOnClickListener { toast("search button pressed") }
		observeViewModelState()
	}

	override fun onResume() {
		super.onResume()
		(activity as AppCompatActivity?)!!.supportActionBar!!.show()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		btDevicesAdapter = null
	}

	private fun initList() {
		btDevicesAdapter = BtDevicesAdapter({ position ->
			showDeviceControlFragment(position)
		},
			{ position ->
				showDeviceControlFragment(position)
			})
		with(deviceBtList) {
			adapter = btDevicesAdapter
			layoutManager = LinearLayoutManager(requireContext())
			setHasFixedSize(true)
			itemAnimator = SlideInLeftAnimator()
		}
	}

	private fun showDeviceControlFragment(position: Int) {
		val btDeviceInformation = btDevicesListViewModel.btDevicesList.value?.get(position)!!
		val action = SearchDevicesFragmentDirections.actionSearchDevicesFragmentToBtDeviceControl(btDeviceInformation)
		findNavController().navigate(action)
	//	findNavController().navigate(R.id.action_searchDevicesFragment_to_btDeviceControl)


	}

	private fun observeViewModelState() {
		btDevicesListViewModel.btDevicesList
			.observe(viewLifecycleOwner) { btDevices -> btDevicesAdapter?.items = btDevices }

		btDevicesListViewModel.showToast
			.observe(viewLifecycleOwner) { toast("SingleLiveEvent") }
	}

	private fun toast(text: String) {
		Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
	}
}