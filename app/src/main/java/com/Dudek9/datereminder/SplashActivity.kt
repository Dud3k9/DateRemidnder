package com.Dudek9.datereminder

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        animateSplashImage()

    }

    private fun animateSplashImage() {
        var alphaAnimation = AlphaAnimation(0.1f, 1f)
        alphaAnimation.duration = 1000
        alphaAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                navigateToMainActivity()
            }
        })
        splash_image.startAnimation(alphaAnimation)
    }

    private fun navigateToMainActivity() {
        var intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
