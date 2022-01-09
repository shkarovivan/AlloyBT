package com.example.alloybt


import android.os.Bundle
import android.os.Handler
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
		val transparencyAnimation =
			AnimationUtils.loadAnimation(requireContext(), R.anim.transparency)
		inv_tech_textView.startAnimation(transparencyAnimation)
		www_alloynn_textView.startAnimation(transparencyAnimation)
		version_textView.startAnimation(transparencyAnimation)

		Handler().postDelayed({
			findNavController().navigate(R.id.action_splashScreenFragment_to_searchDevicesFragment)
		}, 2500)
	}

	override fun onResume() {
		super.onResume()
		(activity as AppCompatActivity?)!!.supportActionBar!!.hide()
	}
}