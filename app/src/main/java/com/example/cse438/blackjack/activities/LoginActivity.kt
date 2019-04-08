package com.example.cse438.blackjack.activities

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast
import com.example.cse438.blackjack.App
import com.example.cse438.blackjack.R
import com.example.cse438.blackjack.adapter.AccountPagerAdapter

import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_signup.*

//LoginActivity
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //sets an adapter for the login
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val adapter = AccountPagerAdapter(this, supportFragmentManager)

        viewpager.adapter = adapter
        tabs.setupWithViewPager(viewpager)



    }

    //alows the user to quit the app
    override fun onBackPressed() {
        // "Close" app (push to background), don't go back to the MainActivity
            finishAffinity()


    }

}
