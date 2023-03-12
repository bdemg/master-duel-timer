package com.example.masterdueltimer

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.masterdueltimer.databinding.ActivityMainBinding
import com.example.masterdueltimer.model.PlayerTimer

class MasterDuelTimer : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentPlayerTimer: PlayerTimer? = null
    private var standbyPlayerTimer: PlayerTimer? = null
    private var onTurnPlayer: PlayerTimer? = null
    private val initialTime:Long = 300
    private var hasGameStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setButtonEnabledStatus(isEnabled: Boolean){

        this.findViewById<Button>(R.id.turnChangeButton).isEnabled = isEnabled
        this.findViewById<Button>(R.id.priorityChangeButton).isEnabled = isEnabled
    }

    fun topPlayerStart(timerView: View){
        if(!hasGameStarted){
            currentPlayerTimer = PlayerTimer(initialTime, timerView as TextView, this.findViewById(R.id.topTurnIndicator))
            standbyPlayerTimer = PlayerTimer(initialTime, this.findViewById(R.id.bottomTimer), this.findViewById(R.id.bottomTurnIndicator))
            onTurnPlayer = currentPlayerTimer

            currentPlayerTimer!!.startTimer()
            currentPlayerTimer!!.turnIndicator.setTextColor(Color.CYAN)
            hasGameStarted = true
            this.setButtonEnabledStatus(true)
        }
    }

    fun bottomPlayerStart(timerView: View){
        if(!hasGameStarted){
            currentPlayerTimer = PlayerTimer(initialTime, timerView as TextView, this.findViewById(R.id.bottomTurnIndicator))
            standbyPlayerTimer = PlayerTimer(initialTime, this.findViewById(R.id.topTimer), this.findViewById(R.id.topTurnIndicator))
            onTurnPlayer = currentPlayerTimer

            currentPlayerTimer!!.startTimer()
            currentPlayerTimer!!.turnIndicator.setTextColor(Color.CYAN)
            hasGameStarted = true
            this.setButtonEnabledStatus(true)
        }
    }

    fun priorityChange(view: View){
        currentPlayerTimer!!.pauseTimer()
        standbyPlayerTimer!!.startTimer()

        val temporalTimer = currentPlayerTimer
        currentPlayerTimer = standbyPlayerTimer
        standbyPlayerTimer = temporalTimer
    }

    fun turnChange(view: View){
        // make sure that the current player is the turn player
        if(currentPlayerTimer!! != onTurnPlayer){
            this.priorityChange(view)
        }

        currentPlayerTimer!!.pauseTimer()
        currentPlayerTimer!!.remainingTime = currentPlayerTimer!!.remainingTime + 30
        if(currentPlayerTimer!!.remainingTime > initialTime) {
            currentPlayerTimer!!.remainingTime = initialTime
        }
        currentPlayerTimer!!.timerView.text = currentPlayerTimer!!.remainingTime.toString()

        standbyPlayerTimer!!.remainingTime = standbyPlayerTimer!!.remainingTime + 60
        if(standbyPlayerTimer!!.remainingTime > initialTime){
            standbyPlayerTimer!!.remainingTime = initialTime
        }

        onTurnPlayer = standbyPlayerTimer
        currentPlayerTimer!!.turnIndicator.setTextColor(Color.LTGRAY)
        standbyPlayerTimer!!.turnIndicator.setTextColor(Color.CYAN)
        priorityChange(view)
    }
}