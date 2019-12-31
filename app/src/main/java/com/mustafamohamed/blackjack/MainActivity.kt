package com.mustafamohamed.blackjack

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import kotlin.math.round
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    //Variables
    var cards = mutableListOf<String>()
    var dealerCards = mutableListOf<String>()
    var gamesPlayed = 0
    var hand = 0
    var dealerHand = 0
    var gameLost = 0
    var gameWin = 0
    var gameTie = 0
    var gameState = -1

    //Game States
    // -1 - Default
    // 0 - Win
    // 1 - Dealer Win (Lost)
    // 2 - Tie
    // 3 - Bust (Lost)

    //Functions
    //Generate Card
    fun getCard():String{
        //Random card and suit generator
        val card = Random.nextInt(1,14)
        val suit = Random.nextInt(0,4)
        //Suits
        // 0 - Spades
        // 1 - Clubs
        // 2 - Diamonds
        // 3 - Hearts

        //Format card and suit into string to save
        var suitLetter = ""
        if(suit == 0){
            suitLetter = "s"
        }
        else if(suit == 1){
            suitLetter = "c"
        }
        else if(suit == 2){
            suitLetter = "d"
        }
        else if(suit == 3){
            suitLetter = "h"
        }

        if(card == 11){
            return "c" + "j" + suitLetter
        }
        else if(card == 12){
            return "c" + "q" + suitLetter
        }
        else if(card == 13){
            return "c" + "k" + suitLetter
        }
        else if(card == 1){
            return "c" + "a" + suitLetter
        }
        return "c" + card.toString() + suitLetter

    }

    //Determines card number equivalence of cards
    fun getCardNum(){
        //Player hand count
        var num = 0
        var temp = ""
        for (card in cards) {
            temp = card.substring(1, card.length - 1) //Removes c at beginning and card suit
            if(temp == "a"){
                num += 0
            }
            else if (temp == "j"){
                num += 10
            }
            else if (temp == "q"){
                num += 10
            }
            else if (temp == "k"){
                num += 10
            }
            else {
                num += temp.toInt()
            }
        }
        //Automatically makes Ace 1 or 11 in players benefit
        for (card in cards) {
            temp = card.substring(1, card.length - 1)
            if(temp == "a"){
                if((num+11) > 21){
                    num += 1
                }
                else{
                    num += 11
                }
            }
        }
        hand = num
        //Dealer hand count
        num = 0
        temp = ""
        for (card in dealerCards) {
            temp = card.substring(1, card.length - 1)
            if(temp == "a"){
                num += 0
            }
            else if (temp == "j"){
                num += 10
            }
            else if (temp == "q"){
                num += 10
            }
            else if (temp == "k"){
                num += 10
            }
            else {
                num += temp.toInt()
            }
        }
        //Automatically makes Ace 1 or 11 in dealers benefit
        for (card in dealerCards) {
            temp = card.substring(1, card.length - 1)
            if(temp == "a"){
                if((num+11) > 21){
                    num += 1
                }
                else{
                    num += 11
                }
            }
        }
        dealerHand = num
    }

    //App Logic
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Declare Button Text Message
        val bottomMsg = findViewById(R.id.bottomMsg) as TextView

        //Declare Card Holder
        val cardHolder = findViewById(R.id.cardHolder) as LinearLayout

        //Declare New Game Button
        val newgameButton = findViewById(R.id.newgameButton) as Button

        //Declare Hit Me Button
        val hitmeButton = findViewById(R.id.hitmeButton) as Button

        //Declare Hold Button
        val holdButton = findViewById(R.id.holdButton) as Button

        //Declare Stats Button
        val statsButton = findViewById(R.id.statsButton) as Button

        //Declare Spaces
        val s1 = findViewById(R.id.s1) as Space
        val s2 = findViewById(R.id.s2) as Space
        val s3 = findViewById(R.id.s3) as Space

        //Declare Buttons Holder
        val buttonHolder = findViewById(R.id.buttonHolder) as LinearLayout

        val params = buttonHolder.getLayoutParams()

        //New Game Button onClick
        newgameButton.setOnClickListener {
            gamesPlayed += 1
            gameState = -1
            //set buttons from invisible to visible
            hitmeButton.setVisibility(View.VISIBLE)
            holdButton.setVisibility(View.VISIBLE)
            params.width = 800
            s2.setVisibility(View.VISIBLE)
            s3.setVisibility(View.VISIBLE)
            cardHolder.removeAllViews() //Removes cards from card holder
            cards = mutableListOf(getCard(), getCard()) //saves two new cards to players hand
            getCardNum() //Get card number of new cards
            bottomMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP,(18).toFloat()) //Reset bottom message text size for card count
            bottomMsg.setText("Card Count: ${hand}")
            for(card in cards){ //Shows card in card holder
                val cardview = ImageView(this)
                cardview.setImageResource(getResources().getIdentifier(card, "mipmap", this.packageName))
                cardHolder.addView(cardview)
            }
        }

        //Hit Me Button onClick Behavior
        hitmeButton.setOnClickListener {
            cards.add(getCard())
            cardHolder.removeAllViews()
            for(card in cards){
                val cardview = ImageView(this)
                cardview.setImageResource(getResources().getIdentifier(card, "mipmap", this.packageName))
                cardHolder.addView(cardview)
            }
            getCardNum()
            bottomMsg.setText("Card Count: ${hand}")

            //Player hand check
            if(hand > 21){
                gameState = 3
                gameLost += 1
                bottomMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP,(30).toFloat())
                bottomMsg.setText("Bust!!!")
                //Set Buttons from visible to invisible
                hitmeButton.setVisibility(View.GONE)
                holdButton.setVisibility(View.GONE)
                params.width = 425
                s2.setVisibility(View.GONE)
                s3.setVisibility(View.GONE)
            }
            else if(hand == 21){
                //Set Buttons from visible to invisible
                hitmeButton.setVisibility(View.GONE)
                holdButton.setVisibility(View.GONE)
                params.width = 425
                s2.setVisibility(View.GONE)
                s3.setVisibility(View.GONE)
                //Dealer Behavior
                dealerCards = mutableListOf(getCard(), getCard())
                getCardNum()
                while(dealerHand <= 15){
                    dealerCards.add(getCard())
                    getCardNum()
                }
                if(hand > dealerHand || dealerHand > 21){
                    gameWin += 1
                    gameState = 0
                    bottomMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP,(30).toFloat())
                    bottomMsg.setText("You Win!!!")
                }
                else if(dealerHand > hand){
                    gameLost += 1
                    gameState = 1
                    bottomMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP,(30).toFloat())
                    bottomMsg.setText("You Lost :(")
                }
                else if(dealerHand == hand){
                    gameTie += 1
                    gameState = 2
                    bottomMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP,(30).toFloat())
                    bottomMsg.setText("Tied Game!!")
                }
            }
        }

        //Hold Button onClick Behavior
        holdButton.setOnClickListener {
            //Dealer Behavior
            dealerCards = mutableListOf(getCard(), getCard())
            getCardNum()
            //Set Buttons from visible to invisible
            hitmeButton.setVisibility(View.GONE)
            holdButton.setVisibility(View.GONE)
            params.width = 425
            s2.setVisibility(View.GONE)
            s3.setVisibility(View.GONE)
            while(dealerHand <= 15){
                dealerCards.add(getCard())
                getCardNum()
            }
            if(hand > dealerHand || dealerHand > 21){
                gameWin += 1
                gameState = 0
                bottomMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP,(30).toFloat())
                bottomMsg.setText("You Win!!!")
            }
            else if(dealerHand > hand){
                gameLost += 1
                gameState = 1
                bottomMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP,(30).toFloat())
                bottomMsg.setText("You Lost :(")
            }
            else if(dealerHand == hand){
                gameTie += 1
                gameState = 2
                bottomMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP,(30).toFloat())
                bottomMsg.setText("Tied Game!!")
            }
        }

        //Stats Button onClick Behavior
        statsButton.setOnClickListener {

            //Dialog Builder
            val statsdialogBuilder = AlertDialog.Builder(this)

            //Set Attributes for Alert
            statsdialogBuilder
                .setMessage("Player wins: ${gameWin}\nDealer wins: ${gameLost}\nTied Games: ${gameTie}\nTotal games played: ${gamesPlayed}\nWin Percentage: ${round((gameWin.toDouble()/gamesPlayed.toDouble()) * 100.0)}")
                .setCancelable(true)
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->  dialog.cancel()})

            //Declare Alert
            val alert = statsdialogBuilder.create()
            //Set title
            alert.setTitle("Statistics")

            //Open Alert
            alert.show()
        }
    }
}
