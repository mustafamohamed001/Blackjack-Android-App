package com.mustafamohamed.blackjack

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
        val card = Random.nextInt(1,14)
        val suit = Random.nextInt(0,4)
        //Suits
        // 0 - Spades
        // 1 - Clubs
        // 2 - Diamonds
        // 3 - Hearts

        var suitLetter = ""
        if(suit == 0){
            suitLetter = "S"
        }
        else if(suit == 1){
            suitLetter = "C"
        }
        else if(suit == 2){
            suitLetter = "D"
        }
        else if(suit == 3){
            suitLetter = "H"
        }
        if(card == 11){
            return "J" + suitLetter
        }
        else if(card == 12){
            return "Q" + suitLetter
        }
        else if(card == 13){
            return "K" + suitLetter
        }
        else if(card == 1){
            return "A" + suitLetter
        }
        return card.toString() + suitLetter

    }

    //Determines card number equivalence of cards
    fun getCardNum(){
        //Player hand count
        var num = 0
        var temp = ""
        for (card in cards) {
            temp = card.substring(0, card.length - 1)
            if(temp == "A"){
                num += 0
            }
            else if (temp == "J"){
                num += 10
            }
            else if (temp == "Q"){
                num += 10
            }
            else if (temp == "K"){
                num += 10
            }
            else {
                num += temp.toInt()
            }
        }
        //Automatically makes Ace 1 or 11 in players benefit
        for (card in cards) {
            temp = card.substring(0, card.length - 1)
            if(temp == "A"){
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
            temp = card.substring(0, card.length - 1)
            if(temp == "A"){
                num += 0
            }
            else if (temp == "J"){
                num += 10
            }
            else if (temp == "Q"){
                num += 10
            }
            else if (temp == "K"){
                num += 10
            }
            else {
                num += temp.toInt()
            }
        }
        //Automatically makes Ace 1 or 11 in dealers benefit
        for (card in dealerCards) {
            temp = card.substring(0, card.length - 1)
            if(temp == "A"){
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Declare Button Text Message
        val buttomMsg = findViewById(R.id.buttomMsg) as TextView

        //Declare New Game Button
        val newgameButton = findViewById(R.id.newgameButton) as Button
        //New Game Button onClick
        newgameButton.setOnClickListener {
            gamesPlayed += 1
            gameState = -1
            cards = mutableListOf(getCard(), getCard())
            getCardNum()
            buttomMsg.setText("Card Count: ${hand}")
            for(card in cards){
                println(card)
            }
            val card = findViewById(R.id.card) as ImageView
            card.setImageResource(getResources().getIdentifier("blue_back", "mipmap", this.packageName))
        }

        //Declare Hit Me Button
        val hitmeButton = findViewById(R.id.hitmeButton) as Button
        hitmeButton.setOnClickListener {
            cards.add(getCard())
            getCardNum()
            buttomMsg.setText("Card Count: ${hand}")
            if(hand > 21){
                gameState = 3
                gameLost += 1
            }
            else if(hand == 21){
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
                }
                else if(dealerHand > hand){
                    gameLost += 1
                    gameState = 1
                }
                else if(dealerHand == hand){
                    gameTie += 1
                    gameState = 2
                }
            }
        }

        //Declare Hold Button
        val holdButton = findViewById(R.id.holdButton) as Button
        //Hold Button onClick
        holdButton.setOnClickListener {
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
            }
            else if(dealerHand > hand){
                gameLost += 1
                gameState = 1
            }
            else if(dealerHand == hand){
                gameTie += 1
                gameState = 2
            }
        }

        //Declare Stats Button
        val statsButton = findViewById(R.id.statsButton) as Button
        //Stats Button onClick
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
