package com.rcent.threadsdownloader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler()
        handler.postDelayed({ // Get Native Ad
            //getNativeAd()
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 1000)

    }
}