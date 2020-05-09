package com.jarredstein.harmonizr.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import com.jarredstein.harmonizr.R
import com.jarredstein.harmonizr.adapter.SliderAdapter

class OnboardingActivity : AppCompatActivity() {

    private var viewPager : ViewPager? = null
    private var constraintLayout : ConstraintLayout? = null
    private var sliderAdapter : SliderAdapter? = null
    private var textView : TextView? = null
    private var dotLayout : LinearLayout? = null
    private var numDots: Int? = null
    private var finishedBtn : Button? = null

    private var prefs: SharedPreferences? = null

    private val viewListener : ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageSelected(position: Int) {
            addDotsIndicator(position,finishedBtn!!)
            Log.i("OnboardingActivity","pos: ${position} | numDots: ${numDots}")
            when(position+1){
                numDots -> finishedBtn?.visibility = View.VISIBLE
            }

        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {

        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        prefs = this.getSharedPreferences(resources.getString(R.string.prefs_filename), Context.MODE_PRIVATE)

        viewPager = findViewById(R.id.slideViewPager)
        dotLayout = findViewById<LinearLayout>(R.id.dotsLayout)
        finishedBtn = findViewById<Button>(R.id.finishedBtn)

        finishedBtn?.visibility = View.GONE
        finishedBtn?.setOnClickListener {

            prefs?.edit()?.putBoolean(getString(R.string.prefs_onboarding_completed),true)!!.apply()
            prefs?.edit()?.putBoolean(getString(R.string.prefs_user_logged_in),true)!!.apply()

            startActivity(Intent(this,MainActivity::class.java))
        }

        sliderAdapter = SliderAdapter(this)

        viewPager?.adapter = sliderAdapter
        addDotsIndicator(0,finishedBtn!!)

        viewPager?.addOnPageChangeListener(viewListener)
    }

    private fun addDotsIndicator(position : Int, finishedBtn: Button) {
        val dots = Array(3) { TextView(this) }
        numDots = dots.size
        dotLayout?.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i].text = Html.fromHtml("&#183")
            dots[i].textSize = 45.0f
            dots[i].setTextColor(resources.getColor(R.color.colorGray))

            dotLayout?.addView(dots[i])
        }

        when {
            dots.isNotEmpty() -> dots[position].setTextColor(resources.getColor(R.color.colorPrimary))
        }


    }



}
