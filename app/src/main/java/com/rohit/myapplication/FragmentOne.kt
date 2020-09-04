package com.rohit.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rohit.myapplication.Module.MessageData
import com.rohit.myapplication.Module.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_one.*
import kotlinx.android.synthetic.main.new_chats_message.view.*


class FragmentOne : Fragment() {
    private lateinit var viewModel:     FragmentOneViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        return inflater.inflate(R.layout.fragment_one, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        latestMessage()
        super.onActivityCreated(savedInstanceState)
    }

    private fun latestMessage() {
        val user = FirebaseAuth.getInstance().currentUser
        val currentUser = user?.uid.toString()
        val adapter = GroupAdapter<ViewHolder>()
        val ref = FirebaseDatabase.getInstance().getReference("/latest/${currentUser}")
        d("chatscreen log ", "${ref.toString()}")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {

                p0.children.forEach {

                    val text = it.child("text").value.toString()

                    val fromUser = FirebaseDatabase.getInstance().getReference("Users/${it.key}")

                    fromUser.addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            val uname = p0.getValue(User::class.java)

//                            d("chatscreen", "${name}")
                            adapter.add(LatestMessage(uname!!,text))
                        }

                    })
                }
            }

        })
        recyler_fragment_one.adapter = adapter
        adapter.setOnItemClickListener{item, view->
            val toUser  = item as LatestMessage
            d("ActivityAllUser", "current user = $item")
            val intent = Intent(view.context, ActivityChatScreen::class.java)
            intent.putExtra("getYourShitDone",toUser.user)
            startActivity(intent)
        }
    }
}

class LatestMessage(val user : User, val text: String): Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.new_chats_message
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.main_display_name.text = user.Name
        viewHolder.itemView.main_display_latest_message.text = text
    }

}

