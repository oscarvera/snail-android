package com.oscarvera.snail.usecases.launch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.oscarvera.snail.R
import com.oscarvera.snail.model.session.SessionManager
import com.oscarvera.snail.usecases.home.MainActivity
import com.oscarvera.snail.usecases.login.LoginActivity

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lunch)

        if (SessionManager.isLogged()||SessionManager.isLocalMode()){
            //Show home
            showHome()
        }else{
            //Show login
            showLogin()
        }



    }

    private fun showHome(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}