package com.example.cse438.blackjack.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.cse438.blackjack.R
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_signup.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.util.Log
import com.example.cse438.blackjack.App
import com.example.cse438.blackjack.activities.MainActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.DocumentReference
import com.google.android.gms.tasks.OnSuccessListener




/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LoginFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

//login fragment
@SuppressLint("ValidFragment")
class LoginFragment(context: Context) : Fragment() {
    private var parentContext = context

    //creates the view
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.example.cse438.blackjack.R.layout.fragment_login, container, false)
    }

    //login user function
    private fun loginUser(){

        //set values
        val email : String = login_email.text.toString()
        val password : String = login_password.text.toString()

        //authenicate with firebase
        App.firebaseAuth?.signInWithEmailAndPassword(email, password)!!.addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val intent = Intent(parentContext, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(parentContext, "Not a valid login", Toast.LENGTH_SHORT).show()
                }
            }


    }

    //onstart lifecycle
    override fun onStart(){
        super.onStart()
        //submit login listener
        submit_login_butto.setOnClickListener{
            if(login_email.text.toString() != "" && login_password.text.toString() != ""){
                loginUser()
            }
            else{
                Toast.makeText(parentContext, "Login cannot be blank", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
