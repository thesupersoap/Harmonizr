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
import com.jarredstein.harmonizr.util.PREFS_FILENAME
import com.jarredstein.harmonizr.util.SPOTIFY_ACCESS_TOKEN
import com.jarredstein.harmonizr.util.USER_LOGGED_IN
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
        prefs = this.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        val isUserLoggedIn = prefs?.getBoolean("USER_LOGGED_IN",false)
        Log.i("MainActivity","is user logged in? : ${isUserLoggedIn}")
        val accessToken = intent.getStringExtra("SPOTIFY_ACCESS_TOKEN")
        //check session state
        if(true != isUserLoggedIn || accessToken == null){
            moveToLoginActivity()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spotifyApi = SpotifyApi()
        spotifyApi.setAccessToken(accessToken)
        val spotify : SpotifyService = spotifyApi.service

       // val imgView = findViewById<ImageView>(R.id.profilePhotoImageView)

        spotify.getAlbum("2dIGnmEIy1WZIcZCFSj6i8", object : Callback<Album> {
           override fun success(album: Album, response: Response?) {
                Log.d("Album success", album.name)
                    // imgView.setImageURI(album.images.get(0).url.toUri())
            }

            override fun failure(error: RetrofitError) {
                Log.d("Album failure", error.toString())
            }
        })
       setLogOutBtn()


        val tokenTextView = findViewById<TextView>(R.id.tokenTextView)
        tokenTextView.text = accessToken
    }
    private fun moveToLoginActivity() {
        prefs!!.edit().putBoolean(USER_LOGGED_IN,false).apply()
        prefs!!.edit().putString(SPOTIFY_ACCESS_TOKEN,"").apply()
        val intent = Intent(
            this,
            LoginActivity::class.java)

        startActivity(intent);

        finish()
    }

    private fun setLogOutBtn(){
        val logOutBtn = findViewById<Button>(R.id.logoutButton)
        logOutBtn.setOnClickListener({
            moveToLoginActivity()

        })
    }

}


