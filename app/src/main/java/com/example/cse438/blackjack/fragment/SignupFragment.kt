package com.example.cse438.blackjack.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cse438.blackjack.App
import com.example.cse438.blackjack.R
import com.example.cse438.blackjack.activities.MainActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.fragment_signup.*


//signup fragment
@SuppressLint("ValidFragment")
class SignupFragment(context: Context)  : Fragment() {
    private var parentContext = context

    //create user
    private fun  createUser(){
        //update the ui
        val userEmail: String = create_email.text.toString()
        val userName: String = create_name.text.toString()
        val userPassword: String = create_password.text.toString()

        //create a user account and database entry
        App.firebaseAuth?.createUserWithEmailAndPassword(userEmail, userPassword)!!.addOnCompleteListener(){ task->
            if (task.isSuccessful) {
                // Create a new user with a first and last name
                val user:HashMap<String,Any> = HashMap<String,Any>() //define empty hashmap
                user.put("chips", 1000)
                user.put("email", userEmail)
                user.put("name", userName)
                user.put("wins", 0)
                user.put("losses", 0)
                // Add a new document with a generated ID
                App.db?.collection("users")
                    ?.document(userEmail)
                    ?.set(user as Map<String, Any>)
                    ?.addOnSuccessListener({ documentReference ->
                        val intent = Intent(parentContext, MainActivity::class.java)
                        startActivity(intent)
                    })
                    ?.addOnFailureListener(OnFailureListener { e -> Log.w("error", "Error adding document", e) })
                val intent = Intent(parentContext, MainActivity::class.java)
                startActivity(intent)
            }
            //if the task fails
            else {
                Toast.makeText(parentContext, "Authorization Failed", Toast.LENGTH_SHORT).show()
            }

        }

    }
    //createview from fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    //onstart lifecycle
    override fun onStart(){
        super.onStart()

        //create account button
        create_account_button.setOnClickListener{
            //success case
            if(create_email.text.toString() != "" && create_name.text.toString() != "" && create_password.text.toString() != ""){
                createUser()
            }
            //error case
            else{
                Toast.makeText(parentContext, "Values cannot be blank", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


