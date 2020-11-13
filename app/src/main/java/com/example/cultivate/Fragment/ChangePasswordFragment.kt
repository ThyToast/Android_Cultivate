package com.example.cultivate.Fragment
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cultivate.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_changepassword.*
import kotlinx.android.synthetic.main.fragment_changepassword.view.*

class ChangePasswordFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_changepassword, container,false)
        view.button_changePassword.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val txtNewPass = newPassword.text
            val txtNewPassConfirm = newPassword2.text

            if (TextUtils.isEmpty(txtNewPassConfirm)) {
                newPassword2.error = "Please enter your new password"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(txtNewPass)) {
                newPassword.error = "Please confirm your password"
                return@setOnClickListener
            }

            user!!.updatePassword(txtNewPass.toString()).addOnSuccessListener {
                Toast.makeText(activity, "Password updated successfully", Toast.LENGTH_SHORT).show()
            }
            newPassword.text.clear()
        }
        return view
    }
}
