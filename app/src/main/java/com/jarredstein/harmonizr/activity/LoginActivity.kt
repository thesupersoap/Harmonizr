package com.jarredstein.harmonizr.activity

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.jarredstein.harmonizr.R
import com.jarredstein.harmonizr.util.*
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE



class LoginActivity : AppCompatActivity()  {
    var prefs: SharedPreferences? = null

    override fun onBackPressed() {
       // super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        prefs = this.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

        animateBg()

        if(!isSpotifyInstalledOnDevice()){
            displayHelpText()
        }

        val btn = findViewById<Button>(R.id.loginBtn)
        btn.setOnClickListener {
            Log.d("LoginActivity","button pressed")
            moveToProfileActivity()
        }

    }

    private fun doMainActivityRedirect(){
        val intent = Intent(
            this,
            MainActivity::class.java)
        startActivity(intent)

        finish()
    }

    private fun doOnBoardingRedirect(){
        val intent = Intent(
            this,
            OnboardingActivity::class.java)
        startActivity(intent)

        finish()
    }


    private fun moveToProfileActivity() {
        prefs!!.edit().putBoolean("USER_LOGGED_IN",true).apply()

        when(prefs?.getBoolean("USER_COMPLETED_ONBOARDING",false)){
            true -> doMainActivityRedirect()
            else -> doOnBoardingRedirect()
        }

    }

    private fun moveToOnboardingActivity(response: AuthenticationResponse) {
        prefs!!.edit().putBoolean("USER_LOGGED_IN",true).apply()
        val intent = Intent(
            this,
            OnboardingActivity::class.java)



        startActivity(intent);
        finish()
    }


    private fun animateBg(){
        val constraintLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.loginPage)
        val animationDrawable: AnimationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)

        animationDrawable.start()
    }

    private fun isSpotifyInstalledOnDevice(): Boolean {
        var result: Boolean
        try{
            packageManager.getPackageInfo(SPOTIFY_PACKAGE_NAME, 0);
            result = true
        } catch(e : Exception){
            result = false
        }
        return result
    }

    private fun displayHelpText() {
        val helpText : TextView = findViewById<TextView>(R.id.getSpotifyTextView)
        helpText.visibility = View.VISIBLE

        helpText.setOnClickListener( View.OnClickListener {
            try{
                startActivity( Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.spotify.music")))
            }
            catch(e : ActivityNotFoundException){
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.spotify.music")))
            }

        })


    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response =
                AuthenticationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthenticationResponse.Type.TOKEN -> {
                    successRedirect(response)
                }
                AuthenticationResponse.Type.ERROR -> {
                }
                else -> {
                }
            }
        }
    }

    private fun successRedirect(response: AuthenticationResponse) {

        when(prefs?.getBoolean(ONBOARD_COMPLETED,false)) {
           true -> moveToProfileActivity()
            else -> moveToOnboardingActivity(response)
        }
    }

}
