package com.example.alloybt

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash_screen)

		val backgroundImage: ImageView = findViewById(R.id.logo_image_view)
		val inverterTechTextView: TextView = findViewById(R.id.inv_tech_textView)
		val wwwAlloynnTextView: TextView = findViewById(R.id.www_alloynn_textView)

		val sideAnimation = AnimationUtils.loadAnimation(this, R.anim.scale)
		backgroundImage.startAnimation(sideAnimation)
		val transparencyAnimation = AnimationUtils.loadAnimation(this, R.anim.transparency)
		inverterTechTextView.startAnimation(transparencyAnimation)
		wwwAlloynnTextView.startAnimation(transparencyAnimation)



		Handler().postDelayed({
			val intent = Intent(this, MainActivity::class.java)
			startActivity(intent)

			finish()
		}, 2500) // delaying for 4 seconds...
	}
}