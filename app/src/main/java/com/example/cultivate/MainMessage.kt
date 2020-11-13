package com.example.cultivate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main_message.*
import kotlinx.android.synthetic.main.chat_latest_message.view.*

class MainMessage : AppCompatActivity() {
    private var drawer: DrawerLayout? = null

    companion object{
        var currentUser:User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_message)

        RecyclerView_main_message.adapter = adapter
        RecyclerView_main_message.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        listenForLatestMessage()
        fetchCurrentUser()


        addMessage2.setOnClickListener {
            val intent = Intent(this, NewMessage::class.java)
            startActivity(intent)
        }

        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this, ChatLog::class.java)
            val row = item as LatestMessageRow

            intent.putExtra(NewMessage.USER_KEY,row.chatPartnerUser)
            startActivity(intent)
        }

        backButton.setOnClickListener {
            val intent = Intent(this, HabitTracker::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }


    }
    class LatestMessageRow(val chatMessage: ChatMessage):Item<ViewHolder>(){
        var chatPartnerUser: User? = null

        override fun getLayout(): Int {
            return R.layout.chat_latest_message
        }

        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.latestMessage.text = chatMessage.text
            val chatPartnerId: String
            if(chatMessage.fromid == FirebaseAuth.getInstance().uid){
                chatPartnerId = chatMessage.toid
            }
            else{
                chatPartnerId = chatMessage.fromid
            }
            val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
            ref.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    chatPartnerUser = p0.getValue(User::class.java)
                    viewHolder.itemView.username.text = chatPartnerUser?.userName

                    val targetImage = viewHolder.itemView.imageView_chat_latest

                    Picasso.get().load(chatPartnerUser?.profileImageUrl).into(targetImage)
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            } )

        }
    }
    val LatestMessagesMap = HashMap<String, ChatMessage>()

    private fun refreshRecyclerView(){
        adapter.clear()
        LatestMessagesMap.values.forEach{
            adapter.add(LatestMessageRow(it))
        }
    }

    private fun listenForLatestMessage(){
        val fromid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromid")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return

                LatestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerView()

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return

                LatestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerView()
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }
    val adapter = GroupAdapter<ViewHolder>()

    private fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)

            }
        })
    }
}
