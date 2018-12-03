package com.example.jmin.shoppinglistapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.splash.*


class SplashScreen: AppCompatActivity(){
    private val splashLength: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        val anim = AnimationUtils.loadAnimation(this@SplashScreen, R.anim.grocery_anim)

        shoppingCart.startAnimation(anim)
        Handler().postDelayed({
            val mainIntent = Intent(this@SplashScreen, MainActivity::class.java)
            this@SplashScreen.startActivity(mainIntent)
            this@SplashScreen.finish()
        }, splashLength)
    }
}