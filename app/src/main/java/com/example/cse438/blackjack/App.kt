package com.example.cse438.blackjack

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//app class to store firebase variables
class App {
    companion object {
        var firebaseAuth: FirebaseAuth? = null
        var db : FirebaseFirestore? = null

    }
}