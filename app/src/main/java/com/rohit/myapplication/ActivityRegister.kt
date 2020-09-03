package com.rohit.myapplication

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class ActivityRegister: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = "Log in"

        register_submit_button.setOnClickListener {
            val email = register_emai.text.toString()
            val password = register_password.text.toString()

            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Signing in")
            progressDialog.setMessage("Please wait")
            progressDialog.setCancelable(false)
            progressDialog.show()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if (!it.isSuccessful) {
                        progressDialog.hide()
                        Toast.makeText(this, "Sign in error/No Account", Toast.LENGTH_LONG).show()
                    }
                    else{
                        intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
        }
    }


}