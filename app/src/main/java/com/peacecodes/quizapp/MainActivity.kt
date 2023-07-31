package com.peacecodes.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.peacecodes.quizapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var Binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        installSplashScreen()
        Binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(Binding.root)
        Binding.quizBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, QuestionActivity::class.java)
            startActivity(intent)
        }
    }
}