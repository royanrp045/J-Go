package com.example.j_go.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.j_go.R
import com.example.j_go.ui.register.RegisterActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            goToMainActivity()
        }, 3000L)
    }

    private fun goToMainActivity() {
        Intent(this, RegisterActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}