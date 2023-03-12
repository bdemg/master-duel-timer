package com.example.masterdueltimer.model

import android.graphics.Color
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView

class PlayerTimer {
    private var timer: CountDownTimer? = null
    val timerView: TextView
    var remainingTime: Long
    var turnIndicator: TextView

    constructor(startingTime:Long, timerView: TextView, turnIndicator: TextView){

        this.remainingTime = startingTime
        this.timerView = timerView
        timerView.text = startingTime.toString()
        this.turnIndicator = turnIndicator
    }

    fun startTimer(){

        this.timer = object : CountDownTimer(this.remainingTime*1000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished / 1000
                timerView.text = remainingTime.toString()
            }

            override fun onFinish() {
                timerView.text = "You Lose!"
                timerView.setTextColor(Color.RED)
            }
        }
        this.timer!!.start()
        this.timerView.setTextColor(Color.CYAN)
    }

    fun pauseTimer(){

        this.timer!!.cancel()
        this.timerView.setTextColor(Color.LTGRAY)
    }
}