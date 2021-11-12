package com.will_d.tp11kakaoplacesearchapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

class PlaceUrlActivity : AppCompatActivity() {


    val wv:WebView by lazy { findViewById(R.id.webView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_url)
        
        //이 액티비티를 실행해준 택배기사님(Intent)에게 가지고 온 추가데이터를 얻어오기
        val placeUrl : String? = intent.getStringExtra("placeUrl") as String

        wv.webViewClient = WebViewClient()
        wv.webChromeClient = WebChromeClient()
        wv.settings.javaScriptEnabled = true
        wv.settings.allowFileAccess = true

        //웹뷰에게 장소정보 url을 읽어서 보여주도록

        if (placeUrl != null) {
            wv.loadUrl(placeUrl)
        }
    }


    override fun onBackPressed() {
        if(wv.canGoBack()) wv.goBack()
        else super.onBackPressed()
    }


}