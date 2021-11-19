package com.will_d.tp11kakaoplacesearchapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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
        startActivity(Intent(this, SignUpActivity::class.java))

    }

    fun clickLoginEmail(view: View) {
        //이메일 로그인 화면(액티비티)로 이동
        startActivity(Intent(this, EmailSignInActivity::class.java))


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
        //***구글로그인 알아서 혼자 해봐
        AlertDialog.Builder(this).setMessage("죄송합니다. 구글 서버문제로 로그인 기능이 불가합니다. 다른 로그인방식을 선택해 주시기 바랍니다.").show()
    }

    fun clickLoginNaver(view: View) {
        //어것도 혼자해봐....................
        AlertDialog.Builder(this).setMessage("죄송합니다. 네이버 서버문제로 로그인 기능이 불가합니다. 다른 로그인방식을 선택해 주시기 바랍니다.").show()

    }

}