package com.example.cultivate.Fragment

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.cultivate.R
import kotlinx.android.synthetic.main.fragment_habit_tracker.*
import kotlinx.android.synthetic.main.fragment_habit_tracker.view.*


class HabitTrackerFragment: Fragment() {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val imm = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        val views:View = inflater.inflate(R.layout.fragment_habit_tracker, container,false)

        views.addHabit.setOnClickListener {
            val inflater:LayoutInflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.popup_add_goals,null)
            val popupWindow = PopupWindow(
                view,
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT )

            popupWindow.isFocusable = true
            popupWindow.update()

            val viewing: View? = activity!!.currentFocus
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.elevation = 20.0F
            }

            // If API level 23 or higher then execute the code
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // Create a new slide animation for popup window enter transition
                val slideIn = Slide()
                slideIn.slideEdge = Gravity.BOTTOM
                popupWindow.enterTransition = slideIn

                // Slide animation for popup window exit transition
                val slideOut = Slide()
                slideOut.slideEdge = Gravity.RIGHT
                popupWindow.exitTransition = slideOut
            }
            views.setOnClickListener{
                // Dismiss the popup window
                popupWindow.dismiss()
            }
            TransitionManager.beginDelayedTransition(rootLayout)
            popupWindow.showAtLocation(
                rootLayout,
                Gravity.CENTER,
                0,
                0
            )
        }
        views.tracker1.setOnLongClickListener {
            val progressTo: Int = progressBar2.progress + 10
            startAnimation(progressBar2, progressTo)
            if (progressBar2?.progress!! >= 100){
                tracker1.visibility = View.GONE
                progressBar2.visibility = View.GONE
                title1.visibility = View.GONE
            }

            return@setOnLongClickListener true
        }
        views.tracker2.setOnLongClickListener {
            val progressTo: Int = progressBar4.progress + 50
            startAnimation(progressBar4, progressTo)
            if (progressBar4?.progress!! >= 100){
                tracker2.visibility = View.GONE
                progressBar4.visibility = View.GONE
                title2.visibility = View.GONE
            }

            return@setOnLongClickListener true
        }
        views.tracker3.setOnLongClickListener {
            val progressTo: Int = progressBar3.progress + 20
            startAnimation(progressBar3, progressTo)
            if (progressBar3?.progress!! >= 100){
                tracker3.visibility = View.GONE
                progressBar3.visibility = View.GONE
                title3.visibility = View.GONE
            }

            return@setOnLongClickListener true
        }
        views.tracker4.setOnLongClickListener {
            val progressTo: Int = progressBar5.progress + 20
            startAnimation(progressBar5, progressTo)
            if (progressBar5?.progress!! >= 100){
                tracker4.visibility = View.GONE
                progressBar5.visibility = View.GONE
                title4.visibility = View.GONE
            }

            return@setOnLongClickListener true
        }
        views.tracker5.setOnLongClickListener {
            val progressTo: Int = progressBar6.progress + 30
            startAnimation(progressBar6, progressTo)
            if (progressBar6?.progress!! >= 100){
                tracker5.visibility = View.GONE
                progressBar6.visibility = View.GONE
                title5.visibility = View.GONE
            }

            return@setOnLongClickListener true
        }
        return views

    }

}

private fun startAnimation(progressBar:ProgressBar, progressTo: Int) {
    val animator = ObjectAnimator.ofInt(progressBar, "progress", progressTo)
        .setDuration(150)
    animator.interpolator = DecelerateInterpolator()
    animator.start()
}
