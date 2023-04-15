package com.example.androidassignment

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.androidassignment.databinding.ActivityStudentMenuBinding

class StudentMenu : AppCompatActivity() {
    private lateinit var binding: ActivityStudentMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        InitializeButtons()
    }
    val context = this
    var db = DataBaseOperator(context)

    fun MainMenu(view: View) {
        startActivity(Intent(this,MainActivity::class.java))
    }

    fun InitializeButtons(){
        var data = db.readQuizNames()
        for(i in 0..data.size - 1){
            InitializeButton(data.get(i))
        }
    }

    fun InitializeButton(s: String) {
        val button: Button = Button(this)
        button.text = s.trim()
        button.setBackgroundColor(Color.parseColor("#ECC03B"))
        binding.StudentMenuLayout.addView(button)
        button.setOnClickListener { StudenQuiz(button.text.toString()) }
    }

    fun StudenQuiz(s: String){
        if(readAmountOfQuestions(s)>0){
        startActivity(Intent(this,StudentQuiz::class.java).putExtra("quizName",s).putExtra("AllQuestions", readAmountOfQuestions(s)).putExtra("randomOrder", randomiseQuestions(s).toIntArray()))
        }else{
            Toast.makeText(context, "Add Questions and Answers to $s", Toast.LENGTH_SHORT).show()
        }
    }

//reads the amount of all questions in a particular quiz that have at least one question and answer
    fun readAmountOfQuestions(s :String): Int{
        var counter = 0
        var data = db.readData()
        for (i in 0..data.size - 1){
            if (data.get(i).quizName == s && data.get(i).questionId != 0 && data.get(i).answerId == 1){
                counter++
            }
        }
        return counter
    }
//creates a list from one to the amount of questions and then shuffles it so questions can be always presented in random order
    fun randomiseQuestions(s: String): List<Int>{
       var list: MutableList<Int> = ArrayList()
        for(i in 1 .. readAmountOfQuestions(s)){
            list.add(i)
        }
        return list.shuffled()
    }
}