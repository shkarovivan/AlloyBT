package com.example.alloybt


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.appcompat.app.AppCompatActivity
import com.example.alloybt.databinding.FragmentSplashScreenBinding


class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

	private var _binding: FragmentSplashScreenBinding? = null
	private val binding get() = _binding!!

	var n = false

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		super.onResume()
		(activity as AppCompatActivity?)!!.supportActionBar!!.hide()

		val sideAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.scale)
		binding.logoImageView.startAnimation(sideAnimation)
		val transparencyAnimation1 =
			AnimationUtils.loadAnimation(requireContext(), R.anim.transparency_1)
		val transparencyAnimation2 =
			AnimationUtils.loadAnimation(requireContext(), R.anim.transparency_2)
		binding.invTechImageView.startAnimation(transparencyAnimation1)
		binding.wwwAlloynnImageview.startAnimation(transparencyAnimation2)
		binding.versionTextView.startAnimation(transparencyAnimation2)

	}


	override fun onResume() {
		super.onResume()

		Handler(Looper.getMainLooper()).postDelayed({
			if (!n){findNavController().navigate(R.id.action_splashScreenFragment_to_searchDevicesFragment)	}
		},3000)
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		n= true
	}
}