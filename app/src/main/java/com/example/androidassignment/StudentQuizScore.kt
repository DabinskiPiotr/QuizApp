package com.example.androidassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.androidassignment.databinding.ActivityStudentQuizScoreBinding

class StudentQuizScore : AppCompatActivity() {
    lateinit var binding: ActivityStudentQuizScoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_quiz_score)
        binding = ActivityStudentQuizScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //presents the score achieved as a fraction and as a percentage
        binding.TitleScore.text = intent.getStringExtra("quizName")
        binding.scoreFraction.text = (intent.getIntExtra("score",1).toString() + "/" + intent.getIntExtra("AllQuestions",1))
        binding.scorePercantage.text = ((((intent.getIntExtra("score",1)).toFloat() / (intent.getIntExtra("AllQuestions",1))) * 100).toString().take(4)+"%")
    }


    fun MainMenuScore(view: View) {
        startActivity(Intent(this,MainActivity::class.java))
    }

    fun StudentMenuScore(view: View) {
        startActivity(Intent(this,StudentMenu::class.java))
    }

}