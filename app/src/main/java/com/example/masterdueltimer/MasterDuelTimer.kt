package com.example.masterdueltimer

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import com.example.masterdueltimer.databinding.ActivityMainBinding
import com.example.masterdueltimer.model.PlayerTimer


class MasterDuelTimer : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentPlayerTimer: PlayerTimer? = null
    private var standbyPlayerTimer: PlayerTimer? = null
    private var onTurnPlayer: PlayerTimer? = null
    private lateinit var sharedPreferences:SharedPreferences

    private var hasGameStarted = false
    private var isGamePaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            if(item.itemId == R.id.action_settings){
                val intent = Intent(this, DuelSettings::class.java)
                startActivity(intent)
            }
            true
        })

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val playPauseButton = this.findViewById<ImageButton>(R.id.playPauseButton)
        playPauseButton.isEnabled = false
        playPauseButton.isClickable = false
        val resetButton = this.findViewById<ImageButton>(R.id.resetGameButton)
        resetButton.isEnabled = false
        resetButton.isClickable = false
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

        this.setTurnAndPriorityButtonStatus(isEnabled)
        val playPauseButton = this.findViewById<ImageButton>(R.id.playPauseButton)
        playPauseButton.isEnabled = isEnabled
        playPauseButton.isClickable = isEnabled
        val resetButton = this.findViewById<ImageButton>(R.id.resetGameButton)
        resetButton.isEnabled = isEnabled
        resetButton.isClickable = isEnabled
        if(isEnabled){
            playPauseButton.setBackgroundColor(this.resources.getColor(R.color.purple_200))
            resetButton.setBackgroundColor(this.resources.getColor(R.color.purple_200))
        } else {
            playPauseButton.background = null
            resetButton.background = null
        }
    }

    private fun setTurnAndPriorityButtonStatus(isEnabled: Boolean){
        this.findViewById<Button>(R.id.turnChangeButton).isEnabled = isEnabled
        this.findViewById<Button>(R.id.priorityChangeButton).isEnabled = isEnabled
    }

    fun topPlayerStart(timerView: View){
        val initialTime = this.sharedPreferences.getString("maxTime", "300")!!.toLong()
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
        val initialTime = this.sharedPreferences.getString("maxTime", "300")!!.toLong()
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
        val maxTime = this.sharedPreferences.getString("maxTime", "300")!!.toLong()
        val turnEndGain = this.sharedPreferences.getString("turnEndGain", "30")!!.toLong()
        val turnStartGain = this.sharedPreferences.getString("turnStartGain", "60")!!.toLong()

        // make sure that the current player is the turn player
        if(currentPlayerTimer!! != onTurnPlayer){
            this.priorityChange(view)
        }

        currentPlayerTimer!!.pauseTimer()
        currentPlayerTimer!!.remainingTime = currentPlayerTimer!!.remainingTime + turnEndGain
        if(currentPlayerTimer!!.remainingTime > maxTime) {
            currentPlayerTimer!!.remainingTime = maxTime
        }
        currentPlayerTimer!!.timerView.text = currentPlayerTimer!!.remainingTime.toString()

        standbyPlayerTimer!!.remainingTime = standbyPlayerTimer!!.remainingTime + turnStartGain
        if(standbyPlayerTimer!!.remainingTime > maxTime){
            standbyPlayerTimer!!.remainingTime = maxTime
        }

        onTurnPlayer = standbyPlayerTimer
        currentPlayerTimer!!.turnIndicator.setTextColor(Color.LTGRAY)
        standbyPlayerTimer!!.turnIndicator.setTextColor(Color.CYAN)
        priorityChange(view)
    }

    fun playPauseDuel(view: View){
        if(!isGamePaused){
            currentPlayerTimer!!.pauseTimer()
            this.setTurnAndPriorityButtonStatus(false)
            this.isGamePaused = true
        } else {
            currentPlayerTimer!!.startTimer()
            this.setTurnAndPriorityButtonStatus(true)
            this.isGamePaused = false
        }
    }

    fun resetDuel(view: View){

        currentPlayerTimer!!.pauseTimer()

        AlertDialog.Builder(this)
            .setTitle("Restart Duel")
            .setMessage("Do you want to restart the duel timers?")
            .setIcon(android.R.drawable.ic_dialog_alert)

            .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, id ->

                    currentPlayerTimer!!.turnIndicator.setTextColor(Color.LTGRAY)
                    currentPlayerTimer!!.timerView.setTextColor(Color.LTGRAY)
                    standbyPlayerTimer!!.turnIndicator.setTextColor(Color.LTGRAY)
                    standbyPlayerTimer!!.timerView.setTextColor(Color.LTGRAY)

                    currentPlayerTimer!!.timerView.setText(R.string.standbyTimerText)
                    standbyPlayerTimer!!.timerView.setText(R.string.standbyTimerText)

                    isGamePaused = false
                    hasGameStarted = false
                    setButtonEnabledStatus(false)
                })
            .setNegativeButton(android.R.string.no, DialogInterface.OnClickListener { dialog, id ->

                    currentPlayerTimer!!.startTimer()
                })
            .show()
    }
}