package com.example.cse438.blackjack.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.AsyncTask
import android.util.Log
import com.example.cse438.blackjack.App
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.synthetic.main.activity_main.*


//view model for leaderboard
class LeaderViewModel (application: Application, sortbyyy: String) : AndroidViewModel(application){
    //set instance variables and data stores
    private var _leaderList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    var sortby = sortbyyy;



    //gets tracks from load tracks
    fun getLeaders(query: String): MutableLiveData<ArrayList<String>> {
        loadTrack(query)
        return _leaderList
    }


    //asych cal to load artist tracks
    private fun loadTrack(query: String){
        ProductAsyncTask().execute()
    }


    //asych call to the top tracks
    @SuppressLint("StaticFieldLeak")
    inner class ProductAsyncTask: AsyncTask<String, Unit, ArrayList<String>>() {
        //asych call
        override fun doInBackground(vararg params: String?): ArrayList<String>? {
            return loadLeaderlist()
        }

        //execute the call
        override fun onPostExecute(result: ArrayList<String>?) {
            if (result == null) {
                Log.e("RESULTS", "No Results Found")
            }
            else {
                //Log.e("RESULTS", result.toString())
                _leaderList.value = result
            }
        }
    }


    //queries the database for the playlist
    private fun loadLeaderlist():  ArrayList<String> {
        var leaderList = ArrayList<String>()
        var leaderMap = HashMap<Double,String>()
        if (sortby == "money") {
            //instance of the database
            App.db!!.collection("users")
                .orderBy("wins", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { documents ->
                    //iterate throught the objects
                    for (document in documents){
                        val leaderString = document.data.get("name").toString() + " with " + document.data.get("wins").toString() + " wins: " + document.data.get("losses").toString() + " losses"
                        var ratio: Double = document.data.get("wins").toString().toDouble()

                        Log.d("YOYO", document.data.get("losses").toString())
                        if(document.data.get("losses").toString() != "0"){
                            ratio = ((document.data.get("wins").toString().toDouble())/(document.data.get("losses").toString().toDouble()))
                        }
                        else
                        {
                            // if someone doesn't have a loss yet, they obviously haven't played much and dont deserve to be at the top of the board
                            ratio = 0.0;
                        }
                        leaderMap.put(ratio,leaderString)
                    }
                    //sort the map by ratio values and conver it to a list
                    var sortedLeaderMap = leaderMap.toSortedMap()
                    var counter = sortedLeaderMap.size
                    for((key, value) in sortedLeaderMap){
                        leaderList.add(counter.toString() + ". " + value)
                        --counter
                    }
                    leaderList.reverse()
                    this._leaderList.value = leaderList
                }
                .addOnFailureListener { exception ->
                    Log.w("a", "Error getting documents: ", exception)
                }
            return leaderList
        }
        else
        {
            //instance of the database
            App.db!!.collection("users")
                .orderBy("chips", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { documents ->
                    var counter = 1
                    for (document in documents) {
                        //Log.d("a", "${document.id} => ${document.data}")
                        val leaderString =  counter.toString() + ". " + document.data.get("name") +" with " + document.data.get("chips").toString() + " chips";
                        //Log.e("here", leaderString)
                        counter++;

                        leaderList.add(leaderString)

                        this._leaderList.value=leaderList

                    }

                }
                .addOnFailureListener { exception ->
                    Log.w("a", "Error getting documents: ", exception)
                }

            return leaderList;
        }
    }

}