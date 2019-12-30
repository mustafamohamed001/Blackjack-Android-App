package com.mustafamohamed.blackjack

import android.content.DialogInterface
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    var cards = ""
    var dealerCards = ""
    var showAlert = false
    var gamesPlayed = 0
    var hand = 0
    var dealerHand = 0
    var gameLost = 0
    var gameWin = 0
    var gameTie = 0
    var gameState = -1
    var cardRow = 0

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

    fun getCardNum():Int{


        return 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Declare New Game Button
        val newgameButton = findViewById(R.id.newgameButton) as Button
        newgameButton.setOnClickListener {
            gamesPlayed += 1
            gameState = -1
            val card = findViewById(R.id.card) as ImageView
            //card.setImageResource(R.mipmap.blue_back)
        }

        //Declare Hit Me Button
        val hitmeButton = findViewById(R.id.hitmeButton) as Button


        //Declare Stats Button
        val statsButton = findViewById(R.id.statsButton) as Button
        //Stats Button onClick
        statsButton.setOnClickListener {

            //Dialog Builder
            val statsdialogBuilder = AlertDialog.Builder(this)

            //Set Attributes for Alert
            statsdialogBuilder
                .setMessage("Player wins: ${gameWin}\nDealer wins: ${gameLost}\nTied Games: ${gameTie}\nTotal games played: ${gamesPlayed}\nWin Percentage: 0")
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
