package com.rohit.myapplication

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log.d
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_setting.*
import java.util.*


class ActivitySetting: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid

        val userDetails = FirebaseDatabase.getInstance().getReference().child("Users").child(uid!!)
        userDetails.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val name = p0.child("Name").value.toString()
                val status = p0.child("Status").value.toString()
                val image = p0.child("Image").value.toString()

                setDetails(name, status)
                Picasso.get().load(image).into(setting_circle)
            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        })

        setting_update_button.setOnClickListener {
            val intentGal = Intent(Intent.ACTION_PICK)
            intentGal.type = "image/*"
            startActivityForResult(Intent.createChooser(intentGal, "Select Image"),1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == RESULT_OK){
            val uri = data?.data
            d("ActivitySetting","$uri")
            CropImage.activity(uri)
                .setAspectRatio(1,1)
                .start(this)

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var downloadURL:String? = null
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {

                val pd = ProgressDialog(this)
                pd.setTitle("Uploading Photo")
                pd.setMessage("Please Wait")
                pd.setCancelable(false)
                pd.show()

                val filename = UUID.randomUUID().toString()
                val resultUri: Uri = result.uri
                val mStorageRef = FirebaseStorage.getInstance().reference
                    .child("profile_image")
                    .child("$filename.jpg")
                mStorageRef.putFile(resultUri).addOnCompleteListener {
                    if (it.isSuccessful){
                        mStorageRef.downloadUrl.addOnSuccessListener { bit ->
                            downloadURL = bit.toString()
                            d("ActivitySetting", "URL is $downloadURL")
                            val uid = FirebaseAuth.getInstance().currentUser?.uid
                            val userDB = FirebaseDatabase.getInstance().reference
                            userDB.child("Users")
                                .child(uid!!)
                                .child("Image")
                                .setValue(downloadURL)
                        }.addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                pd.dismiss()
                                Toast.makeText(this, "Update Successfull", Toast.LENGTH_LONG).show()
                            }
                            else{
                                pd.dismiss()
                                Toast.makeText(this, "Update Successfull", Toast.LENGTH_LONG).show()
                            }

                        }

                    }else{
                        pd.dismiss()
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                d("ActivitySetting","Error is $error")
            }
        }


    }


    private fun setDetails(name: String, status: String) {
        setting_display_name.text = name
        setting_status.text = status
    }
}

