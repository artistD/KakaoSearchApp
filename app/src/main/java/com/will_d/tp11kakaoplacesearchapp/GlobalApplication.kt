package com.will_d.tp11kakaoplacesearchapp

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        //카카오 SDK 초기화 설정
        KakaoSdk.init(this, "4b629446986629e7775ae73eb04d862b")
    }

}