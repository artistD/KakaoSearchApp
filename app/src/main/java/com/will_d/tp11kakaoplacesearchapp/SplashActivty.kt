package com.will_d.tp11kakaoplacesearchapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivty : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //1.5초 후에 코드가 실행되도록 하겠습니다.
        Handler(Looper.getMainLooper()).postDelayed({ //안에 runmethod임 //근데  SAN변환 원리가 뭐임?

            startActivity(Intent(this@SplashActivty, MainActivity::class.java))
            finish()
        }, 1200)




    }
}