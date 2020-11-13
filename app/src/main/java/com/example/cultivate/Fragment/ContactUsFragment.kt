package com.example.cultivate.Fragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cultivate.R
import kotlinx.android.synthetic.main.fragment_contact_us.view.*

class ContactUsFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_contact_us, container,false)
        view.textView6.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.facebook.com/Cultivate-102314218039164")
            startActivity(openURL)
        }

        view.textView4.setOnClickListener {
            val openURL = Intent(Intent.ACTION_DIAL)
            openURL.data = Uri.parse("tel:0123456789")
            startActivity(openURL)
        }

        view.textView5.setOnClickListener {
            val openURL = Intent(Intent.ACTION_SENDTO)
            openURL.data = Uri.parse("mailto:cultivatetgt@gmail.com")
            startActivity(openURL)
        }

        return view
    }
}