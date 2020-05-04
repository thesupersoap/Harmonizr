package com.jarredstein.harmonizr.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.jarredstein.harmonizr.R

class SliderAdapter(context : Context) : PagerAdapter() {

    private val context : Context? = context
    private val layoutInflater : LayoutInflater? = null

    private val onboardingTitles = arrayOf("Welcome", "How it works", "What to do")

    private val descriptionText = arrayOf("Harmonizr is a revolutionary new dating app for music lovers",
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")

    private val onboardingIcons = arrayOf(
        R.drawable.ic_music_note_black_24dp,
        R.drawable.ic_favorite_black_24dp,
        R.drawable.ic_music_note_black_24dp)

    override fun getCount(): Int {
        return onboardingTitles.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view == o as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view : View = layoutInflater.inflate(R.layout.onboarding_slide_layout,container,false)

        val onboardingIconImageView = view.findViewById<ImageView>(R.id.onboardingIconImageView)
        val onboardingHeaderTextView = view.findViewById<TextView>(R.id.onboardingHeaderTextView)
        val onboardingDescriptionTextView = view.findViewById<TextView>(R.id.onboardingDescriptionTextView)
        onboardingIconImageView.setImageResource(onboardingIcons[position])
        onboardingHeaderTextView.text = onboardingTitles[position]
        onboardingDescriptionTextView.text = descriptionText[position]

        container.addView(view)
        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        super.destroyItem(container, position, `object`)
        container.removeView(`object` as ConstraintLayout )

    }


}