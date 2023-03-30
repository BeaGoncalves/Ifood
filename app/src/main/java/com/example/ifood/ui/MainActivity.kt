package com.example.ifood.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceActivity.Header
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.ifood.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        screenActivity()


    }

    private fun screenActivity() {
        startActivity(Intent(this, AutenticacaoActivity::class.java))
    }
}