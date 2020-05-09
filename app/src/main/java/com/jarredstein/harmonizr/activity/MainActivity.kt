package com.jarredstein.harmonizr.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jarredstein.harmonizr.R
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.SpotifyService
import kaaes.spotify.webapi.android.models.Album
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response


class MainActivity : AppCompatActivity() {
    var prefs: SharedPreferences? = null

    override fun onBackPressed() {
        // DISABLED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = this.getSharedPreferences(resources.getString(R.string.prefs_filename), Context.MODE_PRIVATE)
        val isUserLoggedIn = prefs?.getBoolean("USER_LOGGED_IN",false)
        val accessToken = intent.getStringExtra("SPOTIFY_ACCESS_TOKEN")

        //check session state
        /*
        if(true != isUserLoggedIn ){
            Log.i("MainActivity","isUserLoggedIn: ${isUserLoggedIn}  | accessToken: ${accessToken}")
            moveToLoginActivity()
        }
*/
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setLogOutBtn()

    }

    private fun moveToLoginActivity() {
        prefs!!.edit().putBoolean(resources.getString(R.string.prefs_user_logged_in),false).apply()

        startActivity(Intent(
            this,
            LoginActivity::class.java))

        finish()
    }

    private fun setLogOutBtn(){
        val logOutBtn = findViewById<Button>(R.id.logoutButton)
        logOutBtn.setOnClickListener {
            moveToLoginActivity()
        }
    }

}


