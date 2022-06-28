package com.example.alloybt.viewpager.device_monitor

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.alloybt.BtDevice
import com.example.alloybt.BtDeviceInformation
import com.example.alloybt.R
import com.example.alloybt.utils.showAlertDialog
import com.example.alloybt.control.ControlManager
import com.example.alloybt.databinding.FragmentDeviceControlBinding
import com.example.alloybt.json_data.*
import com.example.alloybt.viewmodel.ControlViewModel
import com.example.alloybt.viewmodel.MonitorMode
import com.example.alloybt.viewmodel.ParamsViewModel
import com.example.alloybt.viewpager.ViewPagerFragmentDirections
import com.squareup.moshi.Moshi
import kotlinx.coroutines.*
import no.nordicsemi.android.ble.livedata.state.ConnectionState
import org.json.JSONObject

open class BtDeviceMonitorFragment : Fragment(R.layout.fragment_device_control),
    GestureDetector.OnGestureListener {

    private var _binding: FragmentDeviceControlBinding? = null
    private val binding get() = _binding!!

    private val controlViewModel: ControlViewModel by activityViewModels()
    private val paramsViewModel: ParamsViewModel by activityViewModels()

    private lateinit var btDevice: BluetoothDevice
    private lateinit var controlManager: ControlManager

    private var requestMonitorData: Boolean = false
    private var isReady: Boolean = false
    private var isPause: Boolean = true
    private var userPassword: String? = null

//    private val sharedPrefs by lazy {
//        requireContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
//    }

    //  private lateinit var gestureDetector: GestureDetector

    val moshi: Moshi = Moshi.Builder().build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initializing the gesture detector
        //  gestureDetector = GestureDetector(requireContext(), GestureDetector.OnGestureListener() )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceControlBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        lifecycleScope.coroutineContext.cancel()
    }

    override fun onResume() {
        super.onResume()
        isPause = false
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
        controlViewModel.monitorMode.postValue(MonitorMode.DEVICE_MONITOR)
        getBatteryLevel()
        Log.d("requestData", "onResume")
    }


    override fun onPause() {
        super.onPause()
        isPause = true
        Log.d("requestData", "onPause")
        //  lifecycleScope.coroutineContext.cancel()
        lifecycleScope.coroutineContext.cancelChildren()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btDeviceInformation: BtDevice = BtDeviceInformation.btDeviceInformation
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        btDevice = btDeviceInformation.device
        Password.macAddress = btDeviceInformation.macAddress
        showConnectingBar()
        hideControlViews()
        isPause = false

        (activity as AppCompatActivity?)!!.supportActionBar!!.title =
            btDeviceInformation.model// + " № " + btDeviceInformation.seriesNumber

        getBatteryLevel()

        binding.curTextView.setOnClickListener {
            if (Password.token == null) {
                requestToken()
            } else {
                val currentValue = TigParamsList.tigParamsMap["1007"]
                val action =
                    ViewPagerFragmentDirections.actionViewPagerFragmentToFragmentBottomTune(
                        currentValue!!
                    )
                findNavController().navigate(action)
            }
        }

        binding.weldTypeTextView.setOnClickListener {
            if (Password.token == null) {
                requestToken()
            } else {
                val tigValue = TigParamsList.tigParamsMap["1000"]
                val action =
                    ViewPagerFragmentDirections.actionViewPagerFragmentToFragmentBottomEnum(tigValue!!)
                findNavController().navigate(action)
            }
        }

        binding.waveFormImageView.setOnClickListener {
            if (Password.token == null) {
                requestToken()
            } else {
                val tigValue = TigParamsList.tigParamsMap["1003"]
                val action =
                    ViewPagerFragmentDirections.actionViewPagerFragmentToFragmentBottomEnum(tigValue!!)
                findNavController().navigate(action)
            }
        }

        binding.diamElectrodeTextView.setOnClickListener {
            if (Password.token == null) {
                requestToken()
            } else {
                val diamValue = TigParamsList.tigParamsMap["101D"]
                val action =
                    ViewPagerFragmentDirections.actionViewPagerFragmentToFragmentBottomTune(
                        diamValue!!
                    )
                findNavController().navigate(action)
            }
        }

        binding.torchModeTextView.setOnClickListener {
            if (Password.token == null) {
                requestToken()
            } else {
                val tigValue = TigParamsList.tigParamsMap["1002"]
                val action =
                    ViewPagerFragmentDirections.actionViewPagerFragmentToFragmentBottomEnum(tigValue!!)
                findNavController().navigate(action)
            }
        }

        binding.lifTigImageView.setOnClickListener {
            if (Password.token == null) {
                requestToken()
            } else {
                val tigValue = TigParamsList.tigParamsMap["1001"]
                val action =
                    ViewPagerFragmentDirections.actionViewPagerFragmentToFragmentBottomEnum(tigValue!!)
                findNavController().navigate(action)
            }
        }

        binding.fastProgramButton.setOnClickListener {
            if (Password.token == null) {
                requestToken()
            } else {
                val action = ViewPagerFragmentDirections.actionViewPagerFragmentToFragmentFastPrograms(true)
                    findNavController().navigate(action)


            }
        }

        controlViewModel.dataFromBtDevice.observe(
            viewLifecycleOwner
        ) { btDataReceived ->
            Log.d("requestDataReceived", btDataReceived)
            parseMonitorData(btDataReceived)

        }

        controlViewModel.monitorMode.observe(viewLifecycleOwner) { mode ->
            requestMonitorData = (mode == MonitorMode.DEVICE_MONITOR)
            if (requestMonitorData) {
                sendMonitorRequest()
            }
        }

        controlViewModel.connectionState.observe(viewLifecycleOwner) {
            when (it.state) {
                ConnectionState.State.CONNECTING -> {
                    showConnectingBar()
                    hideControlViews()
                    binding.btStateTextView.text = resources.getText(R.string.connecting_state)
                    isReady = false

                }
                ConnectionState.State.INITIALIZING -> {
                    showConnectingBar()
                    hideControlViews()
                    binding.btStateTextView.text = resources.getText(R.string.initialization_state)
                    isReady = false
                }
                ConnectionState.State.READY -> {
                    showControlViews()
                    binding.btStateTextView.text = ""
                    isReady = true
                    controlViewModel.monitorMode.postValue(MonitorMode.DEVICE_MONITOR)
                    val password = checkBtPassword()
                    if (password != null){
                        Password.password = password
                        sendPassword(Password.password!!)
                    }
                }
                ConnectionState.State.DISCONNECTED -> {
                    showConnectingBar()
                    hideControlViews()
                    binding.btStateTextView.text = resources.getText(R.string.disconnected_state)
                    isReady = false
                }
                ConnectionState.State.DISCONNECTING -> {
                    showConnectingBar()
                    hideControlViews()
                    binding.btStateTextView.text = resources.getText(R.string.disconnecting_state)
                    isReady = false
                }
                else -> binding.btStateTextView.visibility = View.INVISIBLE
            }
        }
    }

    private fun getPasswordRequestJsonString(password: String): String {
        val string = "{\"Write\":{\"Password\":$password}}"
        Log.d("requestData", string)
        return string

    }

    private fun sendPassword(password: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            controlViewModel.setWeldData(getPasswordRequestJsonString(password))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        controlManager = ControlManager(context)
    }

    override fun onStart() {
        super.onStart()
        controlViewModel.connect(btDevice)
    }

    private fun sendText(data: String) {
        controlViewModel.setWeldData(data)
    }

    private fun showConnectingBar() {
        binding.btStateTextView.visibility = View.VISIBLE
        binding.stateProgressBar.visibility = View.VISIBLE
    }

    private fun hideControlViews() {
        with(binding) {
            curTextView.visibility = View.INVISIBLE
            currentHintTextView.visibility = View.INVISIBLE
            upPanelImageView.visibility = View.INVISIBLE
            waveFormImageView.isVisible = false
            lifTigImageView.isVisible = false
            weldTypeTextView.isVisible = false
            diamElectrodeTextView.isVisible = false
            torchModeTextView.isVisible = false
            weldOfImageView.isVisible = false
            weldOnImageView.isVisible = false
            wtOffImageView.isVisible = false
            wtOnImageView.isVisible = false
            lockClosedImageView.isVisible = false
            lockOpenImageView.isVisible = false
            batteryImage.isVisible = false
            batteryTextView.isVisible = false
            scale1Image.isVisible = false
            scale2Image.isVisible = false
        }
    }

    private fun showControlViews() {
        with(binding) {
            btStateTextView.visibility = View.INVISIBLE
            stateProgressBar.visibility = View.INVISIBLE
            curTextView.visibility = View.VISIBLE
            currentHintTextView.visibility = View.VISIBLE
            upPanelImageView.visibility = View.VISIBLE

            waveFormImageView.isVisible = true
            lifTigImageView.isVisible = true
            weldTypeTextView.isVisible = true
            diamElectrodeTextView.isVisible = true
            torchModeTextView.isVisible = true
            weldOfImageView.isVisible = true
            batteryImage.isVisible = true
            batteryTextView.isVisible = true
            scale1Image.isVisible = true
            scale2Image.isVisible = true
            wtOffImageView.isVisible = false
            lockClosedImageView.isVisible = false
            lockOpenImageView.isVisible = false

        }
    }

    private fun parseMonitorData(data: String) {
        val moshi = Moshi.Builder().build()
        val tigFastParamsAdapter = moshi.adapter(TigMonitorParams::class.java).nonNull()

        try {
            val tigFastParams = tigFastParamsAdapter.fromJson(data)
            if (tigFastParams != null) {
                Log.d("requestData", "fromJSON Ok")
                showParams(tigFastParams)
            } else {
                toast("WeldParam = null")
            }

        } catch (e: Exception) {
            try {
                val jsonObject = JSONObject(data)
                val token = jsonObject.getLong("Token")
                Password.token = token
                if( userPassword != null){
                    Password.password = userPassword
                    savePassword(userPassword!!)
                }
                toast(getString(R.string.token_success_text))
                Log.d("token", "token ok")
            } catch (e: Exception) {
                try {
                    val jsonObject = JSONObject(data)
                    val token = jsonObject.getString("Token")
                    if (token == "Error") {
                        Password.password = null
                        toast(getString(R.string.token_fail_text))
                    }
                } catch (e: Exception) {
                }
//
//                val tokenAdapter = moshi.adapter(Token::class.java).nonNull()
//                val token = tokenAdapter.fromJson(data)
//                if (token != null) {
//                    Password.token = token.token
//                    toast(getString(R.string.token_success_text))
//                    Log.d("token", "token ok")
//                }
//                Log.d("token", "token get")
            } catch (e: Exception) {
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showParams(params: TigMonitorParams) {

        with(binding) {
            Log.d("requestData", params.toString())
            if (params.value.state > 2) {
                val current = params.value.realCurrent.toInt()
                curTextView.text = current.toString()
                weldOfImageView.isVisible = false
                weldOnImageView.isVisible = true
                scale1Image.setImageLevel(current)
                scale2Image.setImageLevel(current)

            } else {
                val current = params.value.workCurrent
                curTextView.text = current.toString()
                weldOfImageView.isVisible = true
                weldOnImageView.isVisible = false
                scale1Image.setImageLevel(current)
                scale2Image.setImageLevel(current)
            }

            waveFormImageView.setImageLevel(params.value.waveForm)
            lifTigImageView.setImageLevel(params.value.liftTig)
            diamElectrodeTextView.text = params.value.diamElectrode.toString() + "мм"

            when (params.value.mode) {
                4 -> {
                    waveFormImageView.visibility = View.VISIBLE
                    weldTypeTextView.text = "AC+DC"
                }
                0 -> {
                    waveFormImageView.visibility = View.VISIBLE
                    weldTypeTextView.text = "AC"
                }
                1 -> {
                    waveFormImageView.visibility = View.VISIBLE
                    weldTypeTextView.text = "AC Pulse"
                }
                3 -> {
                    waveFormImageView.visibility = View.INVISIBLE
                    weldTypeTextView.text = "DC Pulse"
                }
                5 -> {
                    waveFormImageView.visibility = View.INVISIBLE
                    weldTypeTextView.text = "MMA"
                }
                2 -> {
                    waveFormImageView.visibility = View.INVISIBLE
                    weldTypeTextView.text = "DC"
                }
                else -> weldTypeTextView.text = "Err"
            }

            torchModeTextView.text =
                when (params.value.weldButtonMode) {
                    0 -> "2T"
                    1 -> "4T"
                    2 -> "Spot"
                    3 -> "Repeat"
                    else -> "Err"
                }

            if (params.value.errors != 0) {
                var errors = ""
                with(params.value.errors) {
                    if ((this and 0x1) > 0) errors += "Перегрев источника\n"
                    if ((this and 0x2) > 0) errors += "Ошибка БВО\n"
                    if ((this and 0x4) > 0) errors += "Ошибка датчика контроля фаз\n"
                    if ((this and 0x8) > 0) errors += "Ошибка потока газа\n"
                    if ((this and 0x10) > 0) errors += "Ошибка давления газа\n"
                }
                errorTextView.text = errors
            } else errorTextView.text = ""

        }
        paramsViewModel.refreshMonitorParams(params)
    }

    private fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    private fun getBatteryLevel() {

        lifecycleScope.launch(Dispatchers.Main) {
            while (true) {
                val batteryStatus = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
                    context?.registerReceiver(null, ifilter)
                }
                val batteryPct = batteryStatus?.let { intent ->
                    val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    binding.batteryTextView.text = "$level%"
                    binding.batteryImage.setImageLevel(level)
                    val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                    Log.d("batteryPct", "battery status = $level $scale")
                    level * 100 / scale.toFloat()
                }
                if (batteryPct != null) {
                    //binding.batteryTextView.text = batteryPct!!.toInt().toString() + "%"
                    //binding.batteryImage.setImageLevel(batteryPct!!.toInt())
                    Log.d("batteryPct", "battery not null = $batteryPct")
                }

                delay(10000)
                if (batteryPct != null) {
                    binding.batteryTextView.text = batteryPct.toInt().toString() + "%"
                }
                Log.d("batteryPct", "battery = $batteryPct")
            }
        }
    }

    private fun sendMonitorRequest() {
        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("requestData", "lifecycleScope start")
            var adapter = moshi.adapter(RequestMonitorParams::class.java).nonNull()
            var requestMonitorJson = ""
            try {
                requestMonitorJson = adapter.toJson(RequestMonitorParams())
            } catch (e: Exception) {
                toast("movie to JSON error = ${e.message}")
            }
            while (requestMonitorData && isReady/*&& !isPause*/) {
                sendText(requestMonitorJson)
                Log.d("requestData", requestMonitorJson)
                delay(200)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun requestToken() {
        showAlertDialog(requireContext()) { password ->
            sendPassword(password)
            userPassword = password
            Log.d("requestDataReceived", password)
        }

    }

    private fun checkBtPassword(): String?{
        val sharedPrefs =
            requireContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        var password: String? = null
        password = sharedPrefs.getString(Password.macAddress, null)
//        lifecycleScope.launch(Dispatchers.IO) {
//            password = sharedPrefs.getString(Password.macAddress, "44109")
//            isPassword = sharedPrefs.contains (Password.macAddress,)
//            password = sharedPrefs.getString("Test", "testFail")
//        }
        Log.d("token", "$password  - ${Password.macAddress}" )
        return password
    }

    private fun savePassword(password: String) {

       val sharedPrefs =
            requireContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

        lifecycleScope.launch(Dispatchers.IO) {
            sharedPrefs.edit()
                .putString(Password.macAddress, password)
                .apply()
        }
    }

    override fun onDown(e: MotionEvent?): Boolean {
        toast("onDown")
        return true
    }

    override fun onShowPress(e: MotionEvent?) {
        toast("onDown")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        toast("onDown")
        return true
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        toast("onDown")
        return true
    }

    override fun onLongPress(e: MotionEvent?) {
        toast("onLongPres")

    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        toast("onDown")
        return true
    }

    companion object {
        const val SHARED_PREFS_NAME = "alloy_passwords"
    }
}

