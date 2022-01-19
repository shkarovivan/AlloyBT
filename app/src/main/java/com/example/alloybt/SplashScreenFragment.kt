package com.example.alloybt


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_splash_screen.*
import androidx.appcompat.app.AppCompatActivity


class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val sideAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.scale)
		logo_image_view.startAnimation(sideAnimation)
		val transparencyAnimation1 =
			AnimationUtils.loadAnimation(requireContext(), R.anim.transparency_1)
		val transparencyAnimation2 =
			AnimationUtils.loadAnimation(requireContext(), R.anim.transparency_2)
		inv_tech_image_view.startAnimation(transparencyAnimation1)
		www_alloynn_Imageview.startAnimation(transparencyAnimation2)
		version_textView.startAnimation(transparencyAnimation2)

		Handler(Looper.getMainLooper()).postDelayed({
			findNavController().navigate(R.id.action_splashScreenFragment_to_searchDevicesFragment)
		}, 3000)
	}

	override fun onResume() {
		super.onResume()
		(activity as AppCompatActivity?)!!.supportActionBar!!.hide()
	}
}