package com.example.alloybt

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.adapter.BtDevicesAdapter
import com.example.alloybt.viewmodel.BtDevicesViewModel
import com.example.alloybt.viewmodel.DeviceViewModelFactory
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_search_devices.*

class SearchDevicesFragment : Fragment(R.layout.fragment_search_devices) {

	private val btDevicesListViewModel: BtDevicesViewModel by viewModels(){
	DeviceViewModelFactory((requireActivity().application as App).adapterProvider)
}

	private var btDevicesAdapter: BtDevicesAdapter? = null

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initList()
		checkLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
		searchFAB.setOnClickListener {
			btDevicesListViewModel.refreshList()
			checkLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
		}
	}

	override fun onResume() {
		super.onResume()
		(activity as AppCompatActivity?)!!.supportActionBar!!.show()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		btDevicesAdapter = null
	}

	@RequiresApi(Build.VERSION_CODES.N)
	override fun onStart() {
		super.onStart()
		observeViewModelState()
		searchProgressBar.setProgress(50,true)
		searchProgressBar.visibility = View.GONE
	}

	override fun onStop() {
		super.onStop()
		btDevicesListViewModel.stopScan()
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
			addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
			setHasFixedSize(true)
			itemAnimator = SlideInLeftAnimator()
		}
	}

	private fun showDeviceControlFragment(position: Int) {
		val btDeviceInformation = btDevicesListViewModel.btDevicesList.value?.get(position)!!
		val action = SearchDevicesFragmentDirections.actionSearchDevicesFragmentToBtDeviceControl(btDeviceInformation)
		findNavController().navigate(action)
	}

	private val checkLocation = registerForActivityResult(
		ActivityResultContracts.RequestPermission()
	) { granted ->
		if (granted) {
			btDevicesListViewModel.startScan()
		}
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