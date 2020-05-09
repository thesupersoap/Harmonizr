package com.jarredstein.harmonizr.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import com.jarredstein.harmonizr.R
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginActivity : AppCompatActivity()  {
    var prefs: SharedPreferences? = null

    val handler = Handler()

    lateinit var loginBtn : Button

    override fun onBackPressed() {
       // super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        prefs = this.getSharedPreferences(
            resources.getString(R.string.prefs_filename),
            Context.MODE_PRIVATE
        )

        animateBg()


        if (!isSpotifyInstalledOnDevice()) {
            displayHelpText()
        }

        loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {

                spotifyLogin()

        }

    }


    private  fun spotifyLogin(){

        val builder = AuthenticationRequest.Builder(
           "e20d881d958c44ab9dd85184133b329b",
            AuthenticationResponse.Type.TOKEN,
            "harmonizr://callback"
        )

        builder.setScopes(arrayOf("streaming"))
        val request = builder.build()
        val contextActivity = this
        performSpotifyLoginActivity(request,contextActivity)


    }

    private  fun performSpotifyLoginActivity(request: AuthenticationRequest, contextActivity : Activity){

            AuthenticationClient.openLoginActivity(
                contextActivity,
                REQUEST_CODE,
                request
            )

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
                    startActivity(Intent(this, MainActivity::class.java).putExtra("SPOTIFY_TOKEN",response.accessToken))
                }
                AuthenticationResponse.Type.ERROR -> {
                }
                else -> {
                }
            }
        }
    }


    private fun doMainActivityRedirect(){
        val intent = Intent(
            this,
            MainActivity::class.java)
        startActivity(intent)

    }

    private fun doOnBoardingRedirect(){
        val intent = Intent(
            this,
            SplashActivity()::class.java)
        startActivity(intent)

        //finish()
    }


    private fun leaveLoginActivity() {
        prefs!!.edit().putBoolean("USER_LOGGED_IN",true).apply()

        startActivity(Intent(this, SplashActivity::class.java)
            .putExtra(getString(R.string.INTENT_KEY_SPLASH_ACTIVITY),
                      getString(R.string.INTENT_VAL_SPOTIFY_LOGIN)))
        //finish()


    }

    private fun moveToOnboardingActivity() {
        prefs!!.edit().putBoolean("USER_LOGGED_IN",true).apply()
        val intent = Intent(
            this,
            SplashActivity()::class.java)

        startActivity(intent);
       // finish()
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
            packageManager.getPackageInfo(resources.getString(R.string.spotify_package_name), 0);
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


    private fun logThread(methodName: String){
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }



}
