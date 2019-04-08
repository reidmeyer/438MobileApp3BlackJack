package com.example.cse438.blackjack.activities

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.widget.CompoundButton
import android.widget.Toast
import com.example.cse438.blackjack.R
import com.example.cse438.blackjack.viewmodel.LeaderViewModel

import kotlinx.android.synthetic.main.activity_leaderboard.*




//leaderboard activity
class Leaderboard() : AppCompatActivity() {
    //to grab variable for switching between wins and money
    var someVariable = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        //switch sort logic
        val extras = intent.extras

        if (extras != null) {
            someVariable = extras.getString("someVariable")
        }

    //view model
        var viewModel = LeaderViewModel(this.application, someVariable)

        //switch sort logic
        if (someVariable == "")
        {
            toggleButton.setChecked(false)
            viewModel.sortby="money"
        }
        else if (someVariable=="wins")
        {
            toggleButton.setChecked(false)
            viewModel.sortby="wins"
        }
        else
        {
            toggleButton.setChecked(true)
            viewModel.sortby="money"

        }

        //switch button for sorting values
        toggleButton.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Toast.makeText(this,"Sorting by Wins",Toast.LENGTH_LONG).show()
                viewModel.sortby="money"
                val intent = intent
                intent.putExtra("someVariable", "money")
                finish()
                startActivity(intent)


            } else {
                Toast.makeText(this,"Sorting by Chips",Toast.LENGTH_LONG).show()
                viewModel.sortby="wins"
                val intent = intent
                intent.putExtra("someVariable", "wins")
                finish()
                startActivity(intent)

            }
        })


    //cancel button to return home
        return_home.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

}
