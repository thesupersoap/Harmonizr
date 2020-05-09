package com.jarredstein.harmonizr.activity;

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jarredstein.harmonizr.R
import com.jarredstein.harmonizr.R.style
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.authentication.LoginActivity


class SplashActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_screen)

        authenticateSpotify()

    }


    private fun authenticateSpotify() {
        val builder = AuthenticationRequest.Builder(
            getString(R.string.spotify_client_id),
            AuthenticationResponse.Type.TOKEN,
            getString(R.string.spotify_callback_url)
        )
        builder.setScopes(arrayOf("streaming","user-read-email","user-read-private"))

        val request = builder.build()
        AuthenticationClient.openLoginActivity(
            this,
            LoginActivity.REQUEST_CODE,
            request
        )
    }


    private fun logThread(methodName: String){
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }


    private fun startMainActivity() {
        val newintent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(newintent)
    }

}
