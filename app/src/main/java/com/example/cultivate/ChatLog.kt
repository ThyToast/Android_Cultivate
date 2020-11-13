package com.example.cultivate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLog : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()
    var toUser:User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recyclerView_ChatLog.adapter = adapter

        toUser = intent.getParcelableExtra<User>(NewMessage.USER_KEY)
        textView2?.text = toUser?.userName

        ListenForMessages()

        sendMessage.setOnClickListener {
            performSendMessage()
        }




        backButton.setOnClickListener {
            val intent = Intent(this, MainMessage::class.java)
            startActivity(intent)

        }


    }

    private fun performSendMessage(){
        val text = sendText.text.toString()
        val fromid = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessage.USER_KEY)
        val toid = user?.uid

        if(fromid == null) return

        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromid/$toid").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toid/$fromid").push()


        val chatMessage = ChatMessage(reference.key!!, text,fromid, toid!!, System.currentTimeMillis()/1000)

        reference.setValue(chatMessage).addOnSuccessListener {
            recyclerView_ChatLog.scrollToPosition(adapter.itemCount - 1)
            sendText.text.clear()
        }
        toReference.setValue(chatMessage)

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromid/$toid")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toid/$fromid")
        latestMessageToRef.setValue(chatMessage)

    }

    private fun ListenForMessages(){
        val fromid = FirebaseAuth.getInstance().uid
        val toid = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromid/$toid")

        ref.addChildEventListener(object:ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)
                if (chatMessage != null){
                    if (chatMessage.fromid == FirebaseAuth.getInstance().uid){
                        val currentUser = MainMessage.currentUser ?: return
                        adapter.add(ChatFromItem(chatMessage.text, currentUser))
                    }
                    else
                    {
                        adapter.add(ChatToItem(chatMessage.text, toUser!!))
                    }
                }

                recyclerView_ChatLog.scrollToPosition(adapter.itemCount - 1)

            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })

    }
}
class ChatMessage(val id:String, val text:String, val fromid:String, val toid:String, val timestamp: Long){
    constructor(): this("","","","",-1)
}

class ChatFromItem(val text:String, val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_from_row.text = text
        val uri = user.profileImageUrl
        val targetImage = viewHolder.itemView.imageView_chat_from
        Picasso.get().load(uri).into(targetImage)
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(val text:String, val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_to_row.text = text
        val uri = user.profileImageUrl
        val targetImage = viewHolder.itemView.imageView_chat_to
        Picasso.get().load(uri).into(targetImage)
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}