package com.example.cultivate

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button3.setOnClickListener{
            //launch register page
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener{
            //login button
            val email = editText.text.toString()
            val password = editText2.text.toString()

            if (TextUtils.isEmpty(editText.text) || (TextUtils.isEmpty(editText2.text))){
                Toast.makeText(this, "Login failed, please try again", Toast.LENGTH_LONG).show()
                return@setOnClickListener
                }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(!it.isSuccessful) return@addOnCompleteListener

                    val intent = Intent(this, HabitTracker::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                    val uid = FirebaseAuth.getInstance().uid
                    Toast.makeText(this, "Welcome back", Toast.LENGTH_LONG).show()

                }.addOnFailureListener{
                    Toast.makeText(this, "Login failed, please try again", Toast.LENGTH_LONG).show()
                }
        }
    }
}
