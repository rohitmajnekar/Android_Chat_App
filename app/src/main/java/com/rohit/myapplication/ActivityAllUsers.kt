package com.rohit.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rohit.myapplication.Module.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_all_users.*
import kotlinx.android.synthetic.main.single_user_recycler.view.*

class ActivityAllUsers : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_users)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("All Users")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        val adapter = GroupAdapter<ViewHolder>()
//        adapter.add(UserItem())
//        all_user_recycler.adapter = adapter

        getUser()

    }

    private fun getUser() {
        val ref = FirebaseDatabase.getInstance().reference.child("Users")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach{
                    val user = it.getValue(User::class.java)
                    if (user != null){
                        adapter.add(UserItem(user))
                    }
                }
                adapter.setOnItemClickListener{item, view->
                    val toUser  = item as UserItem
                    d("ActivityAllUser", "current user = $item")
                    val intent = Intent(view.context, ActivityChatScreen::class.java)
                    intent.putExtra("getYourShitDone",toUser.user)
                    startActivity(intent)
                    finish()
                }
                all_user_recycler.adapter = adapter
            }

        })
    }
}

class UserItem(val user: User): Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.single_user_recycler
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.user_name.text = user.Name
        viewHolder.itemView.user_status.text = user.Status
        Picasso.get().load(user.Image).into(viewHolder.itemView.user_image)
    }

}
