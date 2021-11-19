package com.will_d.tp11kakaoplacesearchapp

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    val toolbar : androidx.appcompat.widget.Toolbar by lazy { findViewById(R.id.toolbar) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            onBackPressed()
        }


        return super.onOptionsItemSelected(item)
    }

    fun clickSignUp(view: View) {
        
        //Firebase Firestore DB에 이메일 사용자 정보 저장하기

        var etEmail = findViewById<EditText>(R.id.et_email)
        var etPassword = findViewById<EditText>(R.id.et_password)
        var etPasswordConfirm = findViewById<EditText>(R.id.et_password_confirm)

        var email = etEmail.text.toString()
        var password = etPassword.text.toString()
        var passwordConfirm = etPasswordConfirm.text.toString()

        if (!password.equals(passwordConfirm)){
            AlertDialog.Builder(this).setMessage("패스워드확인 문제가 있습니다. 다시 확인하여 입력해주시기 바랍니다.").show()
            etPasswordConfirm.requestFocus()
            etPasswordConfirm.selectAll()
            return
        }
        
        var db :FirebaseFirestore = FirebaseFirestore.getInstance()
        
        //먼저 같은 이메일이 있는지 확인....
        db.collection("emailUsers").get().addOnSuccessListener { 
            //emailUser컬렉션안에 여러개의 document가 존재하기에
            //queryDocumentSnapshots는 리스트임
            
            
            //todo:여기 질문해야함

            var documents : MutableList<DocumentSnapshot> = it.documents
            for (i in 0 until documents.size){
                var document : DocumentSnapshot = documents.get(i)

                AlertDialog.Builder(this@SignUpActivity).setMessage("중복된 아이디가 있습니다.").show()
                etEmail.requestFocus()
                etEmail.selectAll()
                return@addOnSuccessListener
            }



            //저장할 값들을(이메일, 비밀번호)를 HashMap으로 저장
            var user = mutableMapOf<String, String>()
            user.put("email", email)
            user.put("password", password)

            db.collection("emailUsers").add(user).addOnSuccessListener {

                AlertDialog.Builder(this@SignUpActivity)
                    .setMessage("축하합니다. \n 회원가입이 완료되었습니다.")
                    .setPositiveButton("확인", object: DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            finish()
                        }

                    })
                    .show()

            }
            
            
            
        }
        







    }
}