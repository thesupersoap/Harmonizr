package com.jarredstein.harmonizr.activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import com.jarredstein.harmonizr.R
import com.jarredstein.harmonizr.adapter.SliderAdapter
import com.jarredstein.harmonizr.util.PREFS_FILENAME

class OnboardingActivity : AppCompatActivity() {

    private var viewPager : ViewPager? = null
    private var constraintLayout : ConstraintLayout? = null
    private var sliderAdapter : SliderAdapter? = null
    private var textView : TextView? = null
    private var dotLayout : LinearLayout? = null

    var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        prefs = this.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

        viewPager = findViewById(R.id.slideViewPager)
        dotLayout = findViewById<LinearLayout>(R.id.dotsLayout)

        sliderAdapter = SliderAdapter(this)

        viewPager?.adapter = sliderAdapter

        addDotsIndicator()
    }

    private fun addDotsIndicator(){
        val dots = Array(3) {TextView(this)}

        for(i in dots.indices){
            dots[i] = TextView(this)
            dots[i].text = Html.fromHtml("&#183")
            dots[i].textSize = 35.0f
            dots[i].setTextColor(resources.getColor(R.color.colorPrimary))

            dotLayout?.addView(dots[i])
        }

    }
}
