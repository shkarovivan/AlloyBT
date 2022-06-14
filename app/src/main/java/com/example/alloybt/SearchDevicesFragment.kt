package com.example.alloybt

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.adapter.BtDevicesAdapter
import com.example.alloybt.databinding.FragmentSearchDevicesBinding
import com.example.alloybt.viewmodel.BtDevicesViewModel
import com.example.alloybt.viewmodel.DeviceViewModelFactory
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_search_devices.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchDevicesFragment : Fragment(R.layout.fragment_search_devices) {

    private var _binding: FragmentSearchDevicesBinding? = null
    private val binding get() = _binding!!

    lateinit var bluetooth: BluetoothManager
    private val btDevicesListViewModel: BtDevicesViewModel by viewModels {
        DeviceViewModelFactory(
            (requireActivity().application as App).adapterProvider,
            activity?.application!!
        )
    }

    private var btDevicesAdapter: BtDevicesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchDevicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bluetooth =
            requireContext().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        initList()
       // setBtListVisible(true)
        checkLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        binding.searchFAB.setOnClickListener {
            if (bluetooth.adapter.isEnabled) {
                binding.enableBtButton.isVisible = false
                btDevicesListViewModel.refreshList()
                btDevicesListViewModel.startScan()
                setBtListVisible(false)
            } else {
                toast(resources.getString(R.string.not_bluetooth_enabled))
                enableBluetoothButton()
            }
        }

        binding.enableBtButton.isVisible = false
        binding.enableBtButton.setOnClickListener {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            bluetoothEnabledResult.launch(enableIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        btDevicesAdapter = null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
        super.onStart()
        observeViewModelState()
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
        with(binding.deviceBtList) {
            adapter = btDevicesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
            setHasFixedSize(true)
            itemAnimator = SlideInLeftAnimator()
        }
    }

    private fun showDeviceControlFragment(position: Int) {
        val btDeviceInformation = btDevicesListViewModel.btDevicesList.value?.get(position)!!
        BtDeviceInformation.btDeviceInformation = btDeviceInformation
        val action = SearchDevicesFragmentDirections.actionSearchDevicesFragmentToViewPagerFragment(
            btDeviceInformation
        )
        findNavController().navigate(action)
    }

    private val checkLocation = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                checkBluetooth.launch(Manifest.permission.BLUETOOTH_SCAN)
            } else isBluetoothEnabled()
        }
    }

    private val checkBluetooth = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                checkBluetoothConnect.launch(android.Manifest.permission.BLUETOOTH_CONNECT)
            } else isBluetoothEnabled()
        }
    }

    private val checkBluetoothConnect = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            isBluetoothEnabled()
        }
    }



    private val bluetoothEnabledResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (bluetooth.adapter.isEnabled) {
                binding.enableBtButton.isVisible =  false
                btDevicesListViewModel.refreshList()
                Log.e("BluetoothScanner", "lifecycleScope")
                btDevicesListViewModel.startScan()
                setBtListVisible(false)
            }
        } else {
            toast(resources.getString(R.string.not_bluetooth_enabled))
        }
    }

    private fun isBluetoothEnabled() {
        val bluetooth =
            requireContext().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        if (bluetooth.adapter.isEnabled) {
            binding.enableBtButton.isVisible =  false
            btDevicesListViewModel.startScan()
            setBtListVisible(true)
            Log.e("BluetoothScanner", "isEnabled")
        } else {
            enableBluetoothButton()
        }
    }

    private fun observeViewModelState() {
        btDevicesListViewModel.btDevicesList
            .observe(viewLifecycleOwner) { btDevices ->
                if (btDevices.isNotEmpty()) {
                    setBtListVisible(true)
                }
                btDevicesAdapter?.items = btDevices
            }

        btDevicesListViewModel.showToast
            .observe(viewLifecycleOwner) { toast("SingleLiveEvent") }
    }

    private fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun setBtListVisible(state: Boolean) {
        binding.deviceBtList.isVisible = state
        binding.notDevicesTextView.isVisible = state.not()
        binding.progressBar.isVisible = state.not()
    }

    private fun enableBluetoothButton(){
        binding.enableBtButton.isVisible = true
        binding.deviceBtList.isVisible = false
        binding.notDevicesTextView.isVisible = false
        binding.progressBar.isVisible = false
    }
}