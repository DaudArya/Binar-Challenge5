package com.binarChallenge.mymovies

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.binarChallenge.mymovies.ui.MainActivity
import kotlinx.android.synthetic.main.sign_up_window.view.*

class LoginActivity : AppCompatActivity() {
    var sharedPreference: SharedPreference? = null
    lateinit var edt_email: EditText
    lateinit var edt_password: EditText
    lateinit var btn_login: Button
    lateinit var btn_sign_up: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreference = SharedPreference(this)
        edt_email = findViewById(R.id.Email)
        edt_password = findViewById(R.id.Password)
        btn_login = findViewById(R.id.Login)
        btn_sign_up = findViewById(R.id.Registerw)

        val str_login_status = sharedPreference!!.getPreferenceString("login_status")
        if (str_login_status!=null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btn_login.setOnClickListener {
            val str_email_id = edt_email.text.toString()
            val str_password = edt_password.text.toString()

            if(str_email_id.equals("") || str_password.equals("")){
                Toast.makeText(this,"Please Enter Details.", Toast.LENGTH_SHORT).show()
            }else {
                val email_id = sharedPreference!!.getPreferenceString("email_id")
                val password = sharedPreference!!.getPreferenceString("password")

                if(email_id.equals(str_email_id) && password.equals(str_password)){
                    sharedPreference!!.save_String("login_status","1")

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else {
                    Toast.makeText(this,"User Not Available.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btn_sign_up.setOnClickListener {
            fun_SignUp_PopupWindow()
        }
    }
    private fun fun_SignUp_PopupWindow() {
        val layoutInflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popView: View = layoutInflater.inflate(R.layout.sign_up_window, null)
        val popupWindow: PopupWindow
        popupWindow = PopupWindow(popView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT,true)
        popupWindow.showAtLocation(popView, Gravity.CENTER, 0, 0)

        val btn_dia_submit = popView.findViewById(R.id.btn_dia_submit) as Button
        btn_dia_submit.setOnClickListener {

            val str_dia_email_id = popView.edt_dia_email_id.text.toString()
            val str_dia_password = popView.edt_dia_password.text.toString()

            if(str_dia_email_id.equals("") || str_dia_password.equals("")){
                Toast.makeText(this,"Please Enter Details.", Toast.LENGTH_SHORT).show()
            }else {
                popupWindow.dismiss()
                sharedPreference!!.save_String("email_id",str_dia_email_id)
                sharedPreference!!.save_String("password",str_dia_password)
                Toast.makeText(this,"Data Saved Successfully.", Toast.LENGTH_SHORT).show()
            }
        }
    }


}