package com.rohit.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rohit.myapplication.Module.MessageData
import com.rohit.myapplication.Module.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_screen.*
import kotlinx.android.synthetic.main.chat_from.view.*
import kotlinx.android.synthetic.main.chat_to.view.*

class ActivityChatScreen : AppCompatActivity() {

    var currentUser:String?= null
    val adapter = GroupAdapter<ViewHolder>()
    var toUser:User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_screen)

        toUser = intent.getParcelableExtra<User>("getYourShitDone")
        val user = FirebaseAuth.getInstance().currentUser
        currentUser = user?.uid.toString()
        setSupportActionBar(toolbar2)
        supportActionBar?.title = toUser?.Name


        recycler_chat_screen.adapter = adapter
        listenForMessage()
        chat_screen_send_button.setOnClickListener {
            performSendMessage()
        }
    }

    private fun listenForMessage() {
        val ref = FirebaseDatabase.getInstance().getReference("/message-data/${toUser?.id}/$currentUser")

        ref.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(MessageData::class.java)

                if (currentUser != chatMessage?.fromID){
                    adapter.add(ChatFrom(chatMessage!!.text))
                }else{
                    adapter.add((ChatTo(chatMessage!!.text)))
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })
    }

    private fun performSendMessage() {
        val ref = FirebaseDatabase.getInstance().getReference("/message-data/${toUser?.id}/$currentUser").push()
        val toRef = FirebaseDatabase.getInstance().getReference("/message-data/$currentUser/${toUser?.id}").push()
//        d("message log ", "${toUser?.id} ${currentUser?.id}")
        val id = ref.key
        val message = chat_screen_editText.text.toString()
        val fromID = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val user = intent.getParcelableExtra<User>("getYourShitDone")
        val toID = user.id
        val messageData = MessageData(message,fromID,toID,id!!,(System.currentTimeMillis() / 1000).toString())
        ref.setValue(messageData).addOnSuccessListener {
            chat_screen_editText.text.clear()
            recycler_chat_screen.scrollToPosition(adapter.itemCount -1)
        }
        toRef.setValue(messageData)

        val latestRef = FirebaseDatabase.getInstance().getReference("/latest/${toUser?.id}/$currentUser")
    val latestToRef = FirebaseDatabase.getInstance().getReference("/latest/$currentUser/${toUser?.id}")
        latestRef.setValue(messageData)
        latestToRef.setValue(messageData)
    }

}

class ChatFrom(val Text:String):Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_from
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chat_from_text.text = Text
    }

}
class ChatTo(val Text:String):Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_to
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chat_to_text.text = Text
    }

}
