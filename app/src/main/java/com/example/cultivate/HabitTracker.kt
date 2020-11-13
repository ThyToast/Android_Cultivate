package com.example.cultivate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.cultivate.Fragment.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_habit_tracker.*


class HabitTracker : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var drawer: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_tracker)

        verifyUserLoggedIn()
        chatbox.setOnClickListener{
            val intent = Intent(this, MainMessage::class.java)
            startActivity(intent)
        }

        //navigation bar code
        val toolbar:androidx.appcompat.widget.Toolbar = (findViewById(R.id.toolBar))
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        val navigationView: NavigationView = findViewById(R.id.navView)
        navigationView.setNavigationItemSelectedListener(this)


        val toggle = ActionBarDrawerToggle(
            this, drawer, findViewById(R.id.toolBar),
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer?.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                HabitTrackerFragment()
            ).commit()
            navigationView.setCheckedItem(R.id.habitTracker)
        }

        fun onBackPressed() {
            if (drawer?.isDrawerOpen(GravityCompat.START)!!) {
                drawer?.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        }
        userSettings.setOnClickListener{
            drawer?.openDrawer(GravityCompat.START)
        }
        }

    private fun verifyUserLoggedIn(){
        //checks if you're logged in
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null){
            val intent = Intent(this, MainActivity::class.java )
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        else{
            Log.d("debug","user is verified")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.habitTracker -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                HabitTrackerFragment()).commit()
                titleView.text = "Habit Tracker"
            }
            R.id.changePassword ->{
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                ChangePasswordFragment()).commit()
                titleView.text = "Change Password"

            }
            R.id.contactUs -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                ContactUsFragment()).commit()
                titleView.text = "Contact Us"

            }
            R.id.helperProgram -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                HelperProgramFragment()).commit()
                titleView.text = "Helper Program"


            }
            R.id.rewards -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                MyRewardsFragment()).commit()
                titleView.text = "My Rewards"

            }

            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        drawer?.closeDrawer(GravityCompat.START)
        return true
    }
}


