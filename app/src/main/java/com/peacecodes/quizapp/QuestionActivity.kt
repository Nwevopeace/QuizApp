package com.peacecodes.quizapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.peacecodes.quizapp.databinding.ActivityQuestionBinding
import java.util.*
import java.util.concurrent.TimeUnit

class QuestionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionBinding
    //    timer
    private var countDownTimer: CountDownTimer? = null
    private val countDownInMilliSecond: Long = 30000
    private val countDownInterval: Long = 1000
    private var timeLeftMilliSeconds: Long = 0
    private var defaultColor: ColorStateList? = null
    private var score = 0
    private var correct = 0
    private var wrong = 0
    private var skip = 0
    private var qIndex = 0
    private var updateQueNo = 1
    // create string for question, answer and options
    private var questions = arrayOf(
        "Q1. All except one is NOT a data type in kotlin programming language?",
        "Q2. What is the full meaning of MVVM in Kotlin Programming language",
        "Q3. One of the following is NOT a mobile programming language?")

    private var answer = arrayOf(
        "Flowchart",
        "Model View ViewModel",
        "Javascript",)

    private var options = arrayOf(
        "Flowchart",
        "Array",
        "Boolean",
        "Number",
        "Model View ViewModel",
        "Model ViewModel View",
        "Mode View ViewModel",
        "Mode ViewModel View",
        "Javascript",
        "Flutter",
        "Java",
        "Kotlin")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }
    @SuppressLint("SetTextI18n")
    private fun showNextQuestion() {
        checkAnswer()
        binding.apply {
            if (updateQueNo < 3) {
                tvNoOfQues.text = "${updateQueNo + 1}/3"
                updateQueNo++
            }
            if (qIndex <= questions.size - 1) {
                tvQuestion.text = questions[qIndex]
                radioButton1.text = options[qIndex * 4]
                radioButton2.text = options[qIndex * 4 + 1]
                radioButton3.text = options[qIndex * 4 + 2]
                radioButton4.text = options[qIndex * 4 + 3]
            } else {
                score = correct
                val intent = Intent(this@QuestionActivity, ResultActivity::class.java)
                intent.putExtra("correct", correct)
                intent.putExtra("wrong", wrong)
                intent.putExtra("skip", skip)
                startActivity(intent)
                finish()
            }
            radiogrp.clearCheck()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun checkAnswer() {
        binding.apply {
            if (radiogrp.checkedRadioButtonId == -1) {
                skip++
                timeOverAlertDialog()
            } else {
                val checkRadioButton =
                    findViewById<RadioButton>(radiogrp.checkedRadioButtonId)
                val checkAnswer = checkRadioButton.text.toString()
                if (checkAnswer == answer[qIndex]) {
                    correct++
                    txtPlayScore.text = "Score : $correct"
                    correctAlertDialog()
                    countDownTimer?.cancel()
                } else {
                    wrong++
                    wrongAlertDialog()
                    countDownTimer?.cancel()
                }
            }
            qIndex++
        }
    }
    @SuppressLint("SetTextI18n")
    private fun initViews() {
        binding.apply {
            tvQuestion.text = questions[qIndex]
            radioButton1.text = options[0]
            radioButton2.text = options[1]
            radioButton3.text = options[2]
            radioButton4.text = options[3]
            // check options selected or not
            // if selected then selected option correct or wrong
            nextQuestionBtn.setOnClickListener {
                if (radiogrp.checkedRadioButtonId == -1) {
                    Toast.makeText(this@QuestionActivity,
                        "Please select an options",
                        Toast.LENGTH_SHORT)
                        .show()
                } else {
                    showNextQuestion()
                }
            }
            tvNoOfQues.text = "$updateQueNo/3"
            tvQuestion.text = questions[qIndex]
            defaultColor = quizTimer.textColors
            timeLeftMilliSeconds = countDownInMilliSecond
            statCountDownTimer()
        }
    }
    private fun statCountDownTimer() {
        countDownTimer = object : CountDownTimer(timeLeftMilliSeconds, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                binding.apply {
                    timeLeftMilliSeconds = millisUntilFinished
                    val second = TimeUnit.MILLISECONDS.toSeconds(timeLeftMilliSeconds).toInt()
                    // %02d format the integer with 2 digit
                    val timer = String.format(Locale.getDefault(), "Time: %02d", second)
                    quizTimer.text = timer
                    if (timeLeftMilliSeconds < 10000) {
                        quizTimer.setTextColor(Color.RED)
                    } else {
                        quizTimer.setTextColor(defaultColor)
                    }
                }
            }
            override fun onFinish() {
                showNextQuestion()
            }
        }.start()
    }
    @SuppressLint("SetTextI18n")
    private fun correctAlertDialog() {
        val builder = AlertDialog.Builder(this@QuestionActivity)
        val view = LayoutInflater.from(this@QuestionActivity).inflate(R.layout.correct_dialog, null)
        builder.setView(view)
        val tvScore = view.findViewById<TextView>(R.id.tvDialog_score)
        val correctOkBtn = view.findViewById<Button>(R.id.correct_ok)
        tvScore.text = "Score : $correct"
        val alertDialog = builder.create()
        correctOkBtn.setOnClickListener {
            timeLeftMilliSeconds = countDownInMilliSecond
            statCountDownTimer()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
    @SuppressLint("SetTextI18n")
    private fun wrongAlertDialog() {
        val builder = AlertDialog.Builder(this@QuestionActivity)
        val view = LayoutInflater.from(this@QuestionActivity).inflate(R.layout.wrong_dialog, null)
        builder.setView(view)
        val tvWrongDialogCorrectAns = view.findViewById<TextView>(R.id.tv_wrongDialog_correctAns)
        val wrongOk = view.findViewById<Button>(R.id.wrong_ok)
        tvWrongDialogCorrectAns.text = "Correct Answer : " + answer[qIndex]
        val alertDialog = builder.create()
        wrongOk.setOnClickListener {
            timeLeftMilliSeconds =
                countDownInMilliSecond
            statCountDownTimer()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
    @SuppressLint("SetTextI18n")
    private fun timeOverAlertDialog() {
        val builder = AlertDialog.Builder(this@QuestionActivity)
        val view = LayoutInflater.from(this@QuestionActivity).inflate(R.layout.time_over_dialog, null)
        builder.setView(view)
        val timeOverOk = view.findViewById<Button>(R.id.timeOver_ok)
        val alertDialog = builder.create()
        timeOverOk.setOnClickListener {
            timeLeftMilliSeconds = countDownInMilliSecond
            statCountDownTimer()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}