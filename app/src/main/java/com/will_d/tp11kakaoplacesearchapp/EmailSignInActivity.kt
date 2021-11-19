package com.will_d.tp11kakaoplacesearchapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Document

class EmailSignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_sign_in)
        
        var toolbar:Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
    }

    
    //업버튼 눌렀을때 액티비티 종료
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    fun clickSignIn(view: View) {
        var etEmail : EditText = findViewById(R.id.et_email)
        var etpassword : EditText = findViewById(R.id.et_password)

        var email :String = etEmail.text.toString()
        var password : String = etpassword.text.toString()

        //Firebase FireStore DB에서 이메일과 비밀번호 확인
        var db:FirebaseFirestore = FirebaseFirestore.getInstance()
        db.collection("emailUsers").get().addOnSuccessListener {
            var documents : List<DocumentSnapshot> = it.documents
            for (i in 0 until documents.size){
                var document : DocumentSnapshot = documents.get(i)
                if (email.equals(document.getData()?.get("email").toString())){
                    if (password.equals(document.getData()?.get("password").toString())){
                        //로그인 성공

                            var id :String = document.id
                            G.user = UserAccount(id, email)
                            var intent : Intent = Intent(this@EmailSignInActivity, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()

                        return@addOnSuccessListener
                    }else{
                        break
                    }
                }
            }
            
            //로그인 실패
            AlertDialog.Builder(this@EmailSignInActivity).setMessage("이메일과 비밀번호를 다시 확인해주시기 바랍니다.").show()
            etEmail.requestFocus()
            etEmail.selectAll()


        }



    }
}