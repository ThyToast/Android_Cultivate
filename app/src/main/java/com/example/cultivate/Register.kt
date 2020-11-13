package com.example.cultivate

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*
import kotlin.concurrent.schedule


class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var progressBar = findViewById<ProgressBar>(R.id.progressBar) as ProgressBar
        val register = findViewById<Button>(R.id.button) as Button

        register.setOnClickListener{
            var fullName = editText6.text.toString()
            var emailadd = editText3.text.toString()
            var password = editText4.text.toString()
            var phonenum = editText5.text.toString()

            if (TextUtils.isEmpty(fullName)){
                editText6.error = "Please enter your full name"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(emailadd)){
                editText3.error = "Please enter your email address"
                return@setOnClickListener
            }


            if (TextUtils.isEmpty(password)){
                editText4.error = "Please enter your password"
                return@setOnClickListener
            }

            if (password.length < 6 ){
                editText4.error = "Please enter a minimum of 6 characters"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(phonenum)){
                editText5.error = "Please enter your phone number"
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE

            //firebase auth
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailadd,password).addOnCompleteListener {
                if (!it.isSuccessful) {
                    progressBar.visibility = View.INVISIBLE
                    return@addOnCompleteListener
                }
                else
                {
                    Toast.makeText(this, "Account creation successful", Toast.LENGTH_SHORT).show()
                    uploadImageToFirebase()
                    Timer("SettingUp", false).schedule(500) {
                        finish()
                    }
                }
            }
                .addOnFailureListener{
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, "${it.message} Please try again", Toast.LENGTH_SHORT).show()
                }

        }
        uploadPhoto.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            selectPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectPhotoUri)

            selectPhoto.setImageBitmap(bitmap)
            uploadPhoto.alpha = 0f
        }
    }

    private fun saveToDatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid?:""
        val myRef = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(editText6.text.toString(), uid, editText5.text.toString(), editText3.text.toString(), profileImageUrl)

        myRef.setValue(user).addOnSuccessListener {
            Log.d("RegisterActivity", "Saved user to firebase database")
            val intent = Intent(this, HabitTracker::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }

    private fun uploadImageToFirebase(){
        if (selectPhotoUri == null) return
        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$fileName")

        ref.putFile(selectPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    saveToDatabase(it.toString())
                }
            }

    }
}

@Parcelize
class User(val userName:String, val uid:String, val phoneNum:String, val emailAdd:String, val profileImageUrl: String):Parcelable{
    constructor(): this("", "","","","")
}
