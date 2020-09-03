package com.rohit.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.rohit.myapplication.Module.MessageData
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.new_chats_message.view.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestAppPermissions()

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Main Page")

        auth = FirebaseAuth.getInstance()

        val mPageAdapter = PageAdapter(supportFragmentManager)
        viewPager.adapter = mPageAdapter
        tabLayout.setupWithViewPager(viewPager)


    }



    public override fun onStart() {
        super.onStart()
        if (auth.currentUser == null){
            val intent = Intent(this, ActivityLogin::class.java)
            startActivity(intent)
            finish()
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)

        if (item != null) {
            if (item.itemId == R.id.menu_sign_out){
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, ActivityRegister::class.java)
                startActivity(intent)
                finish()
            }
            if(item.itemId == R.id.main_account_setting){
                val intent = Intent(this, ActivitySetting::class.java)
                startActivity(intent)
            }
            if(item.itemId == R.id.menu_all_users){
                val intent = Intent(this, ActivityAllUsers::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    private fun requestAppPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }
        if (hasReadPermissions() && hasWritePermissions()) {
            return
        }
        val REQUEST_WRITE_STORAGE_REQUEST_CODE = 112
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), REQUEST_WRITE_STORAGE_REQUEST_CODE
        ) // your request code
    }

    private fun hasReadPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            baseContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasWritePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            baseContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }



}
