package com.example.cse438.blackjack.activities

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.view.GestureDetectorCompat
import com.example.cse438.blackjack.App
import com.example.cse438.blackjack.R
import com.example.cse438.blackjack.util.CardRandomizer
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.firestore.QuerySnapshot
import com.google.android.gms.tasks.OnCompleteListener
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener

//main activity
class MainActivity : AppCompatActivity() {
    //player and game variables
    private var deck = CardRandomizer().getIDs(this)
    private var playerScore = 0
    private var playerHandVals = ArrayList<String>()
    private var dealerHandVals = ArrayList<String>()
    private var dealerHandIds = ArrayList<Int>()
    private var playerHandIds = ArrayList<Int>()
    private var dealerScore = 0
    private var drawIndex = 0

    //display variables
    private var dealerX = 30
    private var dealerY = 550
    private var playerX = 30
    private var playerY = 1075
    private lateinit var mDetector: GestureDetectorCompat

    //booleans and game play variables
    private var detectorbool = false;
    private var playerTurnEnded = false
    private var username = ""
    private var deltCards = ArrayList<ImageView>()
    private var gameNotDecided = true
    private var betforround = 0;


    //drawcard
    fun drawCard(): Int{
        val card = deck.get(drawIndex)
        ++drawIndex
        return card
    }

    //updatescore
    fun updateScore(card: String, isPlayer: Boolean) {
        println(card + " is card")
        var numString = ""
        var royalString =""
        var isRoyal = false
        for (s in card) {
            //if its a royal card
            if(s.equals("_")){
                isRoyal = true
            }
            //if its a digit
            if(s.isDigit() && !isRoyal){
                numString += s

            }
        }
        //if the card is royal
        if(isRoyal || numString == ""){
            royalString = card.split("_").get(1)
            println(royalString + " is royal string")
            //if its the players turn
            if(royalString == "ace"){
                println("ace was drawn")
                if(isPlayer){
                    playerScore += 11
                }
                else{
                    dealerScore += 11
                }
            }
            //else any other royal card
            else{
                if(isPlayer){
                    playerScore += 10
                    return
                }
                else{
                    dealerScore += 10
                    return
                }
            }
        }

        //increment normal player cards
        else{
            if(isPlayer){
                println(numString + " is the numString")
                val num = numString.toInt()
                playerScore += num
                return
            }
            else{
                println(numString + " is the numString")
                val num = numString.toInt()
                dealerScore += num
                return
            }
        }
    }

    //deals a card to the player
    fun getAnimationPlayer(): AnimatorSet? {
        //if the number of cards is overflowing
        if(playerX > 830){
            playerX = 30
            playerY = 800
        }
        //draws a card
        val card1 = drawCard()
        val card1Val = resources.getResourceEntryName(card1)
        playerHandIds.add(card1)
        playerHandVals.add(card1Val)
        updateScore(card1Val, true)
        println("new player score is " + playerScore)

        //sets the images
        val constraintLayout = findViewById<ConstraintLayout>(R.id.main_layout)
        val playerCardImageView1 = ImageView(this)
        playerCardImageView1.setImageResource(card1)
        constraintLayout.addView(playerCardImageView1)
        playerCardImageView1.layoutParams.width = 220
        playerCardImageView1.layoutParams.height = 220
        val xVal : Int = 15
        val yVal : Int = 210

        //sets the image views
        playerCardImageView1.x = xVal.toFloat()
        playerCardImageView1.y = yVal.toFloat()
        playerCardImageView1.id = playerX
        deltCards.add(playerCardImageView1)

        val anim1 = AnimatorSet()

        //x animation
        val x = ObjectAnimator.ofFloat(
            playerCardImageView1,
            "translationX",
            playerCardImageView1.translationX,
            playerX.toFloat()
        )

        //y animation
        val y = ObjectAnimator.ofFloat(
            playerCardImageView1,
            "translationY",
            playerCardImageView1.translationY,
            playerY.toFloat()
        )

        //run animation
        anim1.playTogether(x,y)
        anim1.duration = 500
        playerX += 200


        //animation listener to check win condition
        anim1.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                if (isPlayerWin()) {
                    playerWon()
                }
                else if (isPlayerBust()) {
                    playerLost()
                }
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationStart(animation: Animator) {}
        })
        return anim1
    }

    //deals car to dealer
    fun getAnimationDealer(): AnimatorSet {

        //deals the card
        val card1 = drawCard()
        val card1Val = resources.getResourceEntryName(card1)
        dealerHandIds.add(card1)
        dealerHandVals.add(card1Val)
        updateScore(card1Val, false)
        println("new dealer score is " + dealerScore)

        //fixes dealer overflow problems
        if(dealerX > 830){
            dealerX = 220
            dealerY = 300
        }

        //sets the layout
        val constraintLayout = findViewById<ConstraintLayout>(R.id.main_layout)
        val playerCardImageView1 = ImageView(this)
        if(dealerHandVals.size == 1){
            playerCardImageView1.setImageResource(R.drawable.back)
        }
        else{
            playerCardImageView1.setImageResource(card1)
        }

        //deals the card on screen
        constraintLayout.addView(playerCardImageView1)
        playerCardImageView1.layoutParams.width = 220
        playerCardImageView1.layoutParams.height = 220
        val xVal : Int = 15
        val yVal : Int = 210
        playerCardImageView1.x = xVal.toFloat()
        playerCardImageView1.y = yVal.toFloat()
        playerCardImageView1.id = dealerX
        deltCards.add(playerCardImageView1)

        //animates the dealing of the card
        val anim1 = AnimatorSet()
        val x = ObjectAnimator.ofFloat(
            playerCardImageView1,
            "translationX",
            playerCardImageView1.translationX,
            dealerX.toFloat()
        )

        val y = ObjectAnimator.ofFloat(
            playerCardImageView1,
            "translationY",
            playerCardImageView1.translationY,
            dealerY.toFloat()
        )

        //plays the animation
        anim1.playTogether(x,y)
        anim1.duration = 500
        dealerX += 200

        //checks win condition after the card is dealt
        anim1.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                if (isDealerWin()) {
                    playerLost()
                }
                else if (isdealerBust()) {
                    //code for player loosing the game
                    playerWon()
                }
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationStart(animation: Animator) {}
        })
        return anim1
    }

    //start game
    fun startGame(){

        //clears all the cards in the game
        for(card in deltCards){
            card.visibility = View.GONE
        }

        //resets teh game
        playerScore = 0
        playerHandVals.clear()
        dealerHandVals.clear()
        dealerHandIds.clear()
        playerHandIds.clear()
        deltCards.clear()
        dealerScore = 0
        drawIndex = 0
        dealerX = 30
        dealerY = 550
        playerX = 30
        playerY = 1075
        gameNotDecided = true
        playerTurnEnded = false

        //betting condition
        if (bet_amount.text.toString()!="")
        {
            if ((bet_amount.text.toString()).toInt()>=0 && ((bet_amount.text.toString()).toInt() <= player_chips.text.toString().substring(7).toInt()))
            {
                betforround=(bet_amount.text.toString()).toInt();
            }

            else
            {
                betforround = 0;
            }

        }
        else
        {
            betforround = 0;
        }

        //updates the ui to reflect the databse
        App.db!!.collection("users")
            .document(App.firebaseAuth!!.currentUser!!.email.toString())
            .get()
                //updaet texts
            .addOnSuccessListener { document ->
               // Log.d("success", document.id + " => " + document.data)
                val playerData = document.data
                player_name.text = "Player Name: " + playerData?.get("name").toString()
                player_wins.text = "Wins: " + playerData?.get("wins").toString()
                player_losses.text = "Losses: " + playerData?.get("losses").toString()
                player_chips.text = "Chips: " + playerData?.get("chips").toString()
                username = playerData?.get("name").toString()
            }

        //check if the betforround is not valid
        if (betforround > player_chips.text.toString().substring(7).toInt())
        {
            betforround=0;
        }


        deck = CardRandomizer().getIDs(this)
        deck.shuffle()

        //animations for dealing cards
        val s = AnimatorSet()
        val s1 = getAnimationPlayer()
        val s2 = getAnimationPlayer()
        s.play(s1).with(s2)
        s.start()
        val t = AnimatorSet()
        val t1 = getAnimationDealer()
        val t2 = getAnimationDealer()
        t.play(t1).with(t2)

        //listeners for when animations end
        t.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                println("animation ended")
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationStart(animation: Animator) {}
        })
        t.start()

        //prins current scores to console
        println(playerHandVals.toString())
        println("player score: " + playerScore)
        println(dealerHandVals.toString())
        println("dealer score: " + dealerScore)
        var playButton = findViewById<Button>(R.id.start_game)
        playButton.isClickable=false
        playButton.visibility = View.GONE
        mDetector = GestureDetectorCompat(this, MyGestureListener())
        detectorbool=true;

        //checks the win condtions
        if(isDealerWin()){
            //send to lose function
            playerLost()
        }
        if(isPlayerWin()){
            //send to win function
            playerWon()
        }
    }

    //check if player win
    fun isPlayerWin(): Boolean{
        if(playerScore == 21){
            return true
        }
        var adjustedScore = playerScore
        if(playerHandVals.contains("hearts_ace")){
            adjustedScore -= 10
            if(adjustedScore == 21){
                return true
            }
        }
        if(playerHandVals.contains("spades_ace")){
            adjustedScore -= 10
            if(adjustedScore == 21){
                return true
            }
        }
        if(playerHandVals.contains("clubs_ace")){
            adjustedScore -= 10
            if(adjustedScore == 21){
                return true
            }
        }
        if(playerHandVals.contains("diamonds_ace")){
            adjustedScore -= 10
            if(adjustedScore == 21){
                return true
            }
        }
        return false
    }

    //check if player bust
    fun isPlayerBust(): Boolean{
        var minPlayerScore = playerScore
        if(playerHandVals.contains("hearts_ace")){
            minPlayerScore -= 10
        }
        if(playerHandVals.contains("spades_ace")){
            minPlayerScore -= 10
        }
        if(playerHandVals.contains("clubs_ace")){
            minPlayerScore -= 10
        }
        if(playerHandVals.contains("diamonds_ace")){
            minPlayerScore -= 10
        }
        if(minPlayerScore > 21){
            return true
        }
        return false
    }

    //check if dealer bust
    fun isdealerBust(): Boolean{
        var minDealerScore = dealerScore
        if(playerHandVals.contains("hearts_ace")){
            minDealerScore -= 10
        }
        if(playerHandVals.contains("spades_ace")){
            minDealerScore -= 10
        }
        if(playerHandVals.contains("clubs_ace")){
            minDealerScore -= 10
        }
        if(playerHandVals.contains("diamonds_ace")){
            minDealerScore -= 10
        }
        if(minDealerScore > 21){
            return true
        }
        return false
    }

    //check if dealer win
    fun isDealerWin(): Boolean{
        if(dealerScore == 21){
            return true
        }
        var adjustedScore = dealerScore
        if(playerHandVals.contains("hearts_ace")){
            adjustedScore -= 10
            if(adjustedScore == 21){
                return true
            }
        }
        if(playerHandVals.contains("spades_ace")){
            adjustedScore -= 10
            if(adjustedScore == 21){
                return true
            }
        }
        if(playerHandVals.contains("clubs_ace")){
            adjustedScore -= 10
            if(adjustedScore == 21){
                return true
            }
        }
        if(playerHandVals.contains("diamonds_ace")){
            adjustedScore -= 10
            if(adjustedScore == 21){
                return true
            }
        }
        if(adjustedScore == 21){
            return true
        }
        return false
    }

    //update the databse if player lostfunction
    fun playerLost(){
        if(gameNotDecided){
            gameNotDecided = false
            var wins = 0
            var chips = 0
            var losses = 0
            var email = App.firebaseAuth!!.currentUser!!.email.toString()
            //update losses for the player
            App.db!!.collection("users")
                .document(email)
                .get()
                .addOnSuccessListener { document ->
                    //Log.d("success", document.id + " => " + document.data)
                    val playerData = document.data
                    println(playerData.toString() + " is the player data")
                    losses = playerData?.get("losses").toString().toInt()
                    chips = playerData?.get("chips").toString().toInt()
                    wins = playerData?.get("wins").toString().toInt()
                    losses = losses + 1
                    //check if valid
                    if (betforround<=chips)
                    {
                        chips = chips - betforround
                    }
                    //callback for query
                    App.db?.collection("users")?.document(email)
                        ?.update("losses", losses, "chips", chips)
                        ?.addOnSuccessListener{println("losses and chips updated")}
                        ?.addOnFailureListener(OnFailureListener { e -> Log.w("error", "Error adding document", e) })

                    //update the ui
                    player_name.text = "Player Name: " + playerData?.get("name").toString()
                    player_wins.text = "Wins: " + playerData?.get("wins").toString()
                    player_losses.text = "Losses: " + playerData?.get("losses").toString()
                    player_chips.text = "Chips: " + playerData?.get("chips").toString()
                    username = playerData?.get("name").toString()

                    //display lost condition
                    Toast.makeText(this,"You Lost!", Toast.LENGTH_SHORT).show()

                    //restart game button
                    var playButton = findViewById<Button>(R.id.start_game)
                    playButton.isClickable=true
                    playButton.visibility = View.VISIBLE
                    mDetector = GestureDetectorCompat(this, MyGestureListener())
                    detectorbool=false;
                }
        }
    }

    //win function for updating database
    fun playerWon(){
        if(gameNotDecided){
            gameNotDecided = false
            var wins = 0
            var losses = 0
            var chips = 0
            var email = App.firebaseAuth!!.currentUser!!.email.toString()

            //updates player stats
            App.db!!.collection("users")
                .document(email)
                .get()
                .addOnSuccessListener { document ->
                    //Log.d("success", document.id + " => " + document.data)
                    val playerData = document.data
                    losses = playerData?.get("losses").toString().toInt()
                    wins = playerData?.get("wins").toString().toInt()
                    chips = playerData?.get("chips").toString().toInt()
                    wins = wins + 1
                    chips = chips + betforround


                    //updates database
                    App.db?.collection("users")?.document(email)
                        ?.update("wins", wins, "chips", chips)
                        ?.addOnSuccessListener{println("wins and chips updated")}
                        ?.addOnFailureListener(OnFailureListener { e -> Log.w("error", "Error adding document", e) })

                    //updates the ui to reflect the database
                    player_name.text = "Player Name: " + playerData?.get("name").toString()
                    player_wins.text = "Wins: " + playerData?.get("wins").toString()
                    player_losses.text = "Losses: " + playerData?.get("losses").toString()
                    player_chips.text = "Chips: " + playerData?.get("chips").toString()
                    username = playerData?.get("name").toString()

                    //display win condition and gestures
                    Toast.makeText(this,"You Won!", Toast.LENGTH_SHORT).show()
                    var playButton = findViewById<Button>(R.id.start_game)
                    playButton.isClickable=true
                    playButton.visibility = View.VISIBLE
                    mDetector = GestureDetectorCompat(this, MyGestureListener())
                    detectorbool=false;
                }
        }

    }

    //restart game automatically
    fun restartGame(){
        //remove delt cards
        for(card in deltCards){
            card.visibility = View.GONE
        }

        //reset player values
        playerScore = 0
        playerHandVals.clear()
        dealerHandVals.clear()
        dealerHandIds.clear()
        playerHandIds.clear()
        deltCards.clear()
        dealerScore = 0
        drawIndex = 0
        dealerX = 30
        dealerY = 550
        playerX = 30
        playerY = 1075
        gameNotDecided = true
        playerTurnEnded = false

        //betting condintion
        if (bet_amount.text.toString()!="")
        {
            if ((bet_amount.text.toString()).toInt()>=0 && ((bet_amount.text.toString()).toInt() <= player_chips.text.toString().substring(7).toInt()))
            {
                betforround=(bet_amount.text.toString()).toInt();
            }

            else
            {
                betforround = 0;
            }

        }
        else
        {
            betforround = 0;
        }
        startGame()
    }

    //oncreate life cycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.cse438.blackjack.R.layout.activity_main)

        //intialize firebase
        FirebaseApp.initializeApp(this)

        bet_amount.setText("0");

        //set all fire abse values
        if(App.db == null){
            App.db = FirebaseFirestore.getInstance()
        }
        if (App.firebaseAuth == null) {
            App.firebaseAuth = FirebaseAuth.getInstance()

        }
        if (App.firebaseAuth != null && App.firebaseAuth?.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        else{
            //get the user values
            App.db!!.collection("users")
                .document(App.firebaseAuth!!.currentUser!!.email.toString())
                .get()
                .addOnSuccessListener { document ->
                    //update the ui
                    val playerData = document.data
                    player_name.text = "Name: " + playerData?.get("name").toString()
                    player_wins.text = "Wins: " + playerData?.get("wins").toString()
                    player_losses.text = "Losses: " + playerData?.get("losses").toString()
                    player_chips.text = "Chips: " + playerData?.get("chips").toString()
                    username = playerData?.get("name").toString()
                }

            //click listener for logout button
            logout_button.setOnClickListener(){
                App.firebaseAuth?.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

            //click listener for leader baord
            leader_board_button.setOnClickListener(){
                var playButton = findViewById<Button>(R.id.start_game)
                if(playButton.isClickable==true)
                {
                    val intent = Intent(this, Leaderboard::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    //onstart
    @SuppressLint
    override fun onStart(){
        super.onStart()
        //starts game and checks betting condition
        start_game.setOnClickListener(){
            if (bet_amount.text.toString()!="")
            {
                if ((bet_amount.text.toString()).toInt()>=0 && ((bet_amount.text.toString()).toInt() <= player_chips.text.toString().substring(7).toInt()))
                {
                    betforround=(bet_amount.text.toString()).toInt();
                    startGame()
                }
            }

        }
    }

    //for gestures
    override fun onTouchEvent(event: MotionEvent) : Boolean {
        if(detectorbool==true)
        {
            mDetector.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }

    private inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

        private var swipedistance = 150

        //ends turn on double tap
        override fun onDoubleTap(e: MotionEvent?): Boolean {
            playerTurnEnded = true
            var animations = ArrayList<AnimatorSet>()
            var totalAnimations = AnimatorSet()
            //plays the dealers turn
            while(dealerScore < 18){
                var hit = getAnimationDealer()
                animations.add(hit)
                //hit.start()
                totalAnimations.play(hit)
            }

            //checks win condition after animation
            totalAnimations.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    if(isDealerWin()){
                        println("the dealer won")
                        playerLost()
                    }
                    else if(isdealerBust()){
                        println("the dealer bust")
                        playerWon()
                    }
                    else if(dealerScore > playerScore){
                        println("the dealer won")
                        playerLost()
                    }
                    else{
                        //tie to the player?
                        println("the player won")
                        playerWon()
                    }
                }
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationStart(animation: Animator) {}
            })
            totalAnimations.start()
            return true
        }

        //hits the player when they swipe right
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if (e2.x - e1.x > swipedistance) {
                println("swipe reached")
                if(!playerTurnEnded){
                    val hit = getAnimationPlayer()
                    hit?.start()
                    return true
                }
            }
            return false
        }

    }
}

