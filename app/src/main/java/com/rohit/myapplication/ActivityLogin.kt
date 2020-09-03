package com.rohit.myapplication

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.widget.Toast
import android.widget.Toast.*
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class ActivityLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Register")


        login_submit_button.setOnClickListener {
            val username = login_username.text.toString()
            val email = login_email.text.toString()
            val password= login_password.text.toString()

            if(email == null || password == null){
                makeText(this, "Please enter all field", LENGTH_LONG).show()
            }
            else{
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Registering User")
                progressDialog.setMessage("Please wait")
                progressDialog.setCancelable(false)
                progressDialog.show()

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener{
                        if (!it.isSuccessful){
                            progressDialog.hide()
                            d("ActivityLocgin","error is = ${it.exception}")
                            Toast.makeText(this,"Register error", LENGTH_LONG).show()
                        }
                        else{
                            addDataToFirebase(username, email)
                            progressDialog.dismiss()
                            intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
            }

        }
        login_already_have_account.setOnClickListener {
            intent = Intent(this, ActivityRegister::class.java)
            startActivity(intent)
        }


    }

    private fun addDataToFirebase(username: String, email: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid
        val mDataBase = FirebaseDatabase.getInstance().reference.child("Users").child(uid!!)
        var hashMap = hashMapOf<String, String>()
        hashMap.put("Name", username)
        hashMap.put("Emai", email)
        hashMap.put("Status","Hey there I am Wasting Time")
        hashMap.put("Image", "Default")
        hashMap.put("Thumb", "thumb-data")
        hashMap.put("id", uid)
        mDataBase.setValue(hashMap).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(this,"Data Uploaded", Toast.LENGTH_LONG)
            }
            return@addOnCompleteListener
        }
    }


}
