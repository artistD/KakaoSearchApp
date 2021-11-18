package com.will_d.tp11kakaoplacesearchapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.kakao.sdk.auth.model.Prompt
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //카카오 keyhash를 얻어오기
//        var str = Utility.getKeyHash(this)
//        Log.i("keyhash", str)

    }

    fun clickGo(view: View) {
        //겸손하게 살아!!!
        //MainActivity로 이동
        val intent:Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun clickSignUp(view: View) {
        //회원가입 화면(액티비티)로 이동

    }

    fun clickLoginEmail(view: View) {
        //이메일 로그인 화면(액티비티)로 이돛

    }

    fun clickLoginKakao(view: View) {

        //kakao계정 로그인 요청
        UserApiClient.instance.loginWithKakaoAccount(this,prompts = listOf(Prompt.LOGIN)){token, error ->
            if (error !=null){
                Log.e("Tag", "로그인 실패", error)
            }else if (token !=null){
                Log.i("Tag", "로그인 성공 ${token.accessToken}")
                    //로그인 사용자 정보 얻어오기
                    UserApiClient.instance.me { user, error ->
                        if (error !=null){

                        }else if (user !=null){
                            var userId :String = ""+user.id
                            var email : String? = user.kakaoAccount?.email

                            //다른 액티비티에서 마음대로 쓸수 있도록 .....
                            G.user = UserAccount(userId, email)

//                            AlertDialog.Builder(this@LoginActivity).setMessage(""+email).show()
                            //Main화면으로 이동
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }


                    }//me........

            }//token
        }

    }

    fun clickLoginGoogle(view: View) {

    }

    fun clickLoginNaver(view: View) {

    }

}