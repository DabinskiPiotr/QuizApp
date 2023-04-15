package com.example.androidassignment

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.androidassignment.databinding.ActivityTeacherMenuBinding

class TeacherMenu : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        InitializeButtons()
    }

    val context = this
    var db = DataBaseOperator(context)
    var exists = false

    fun MainMenu(view: View) {
        startActivity(Intent(this,MainActivity::class.java))
    }

    fun AddNewQuiz(view: View) {
        binding.PopupAddQuiz.visibility = View.VISIBLE
    }

    fun backButton(view: View) {
        binding.NewQuizName.text!!.clear()
        binding.PopupAddQuiz.visibility = View.INVISIBLE

    }
//adds quiz to the database if it does not already exist or is an empty string
    fun AddQuiz(s: String) {
        var quizDB = QuizDB(s.trim())
        var data = db.readQuizNames()
        for(i in 0 .. data.size - 1) {
            if(data[i].equals(s.trim(), true) || s.trim().equals("")){
                exists = true
            }
        }
        if (!exists)
        {
            db.insertData(quizDB)
        }else{
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
    }
    //Creates a button if quiz does not exist
    fun CreateButton(view: View) {
            AddQuiz(binding.NewQuizName.text.toString())
        if(!exists) {
            startActivity(Intent(this, Quiz::class.java).putExtra("quizName", binding.NewQuizName.text.toString()))
        }
        exists = false
    }

    fun QuizMenu(s: String){
        startActivity(Intent(this,Quiz::class.java).putExtra("quizName",s))
    }
//initializes a button
    fun InitializeButton(s: String) {
        val button: Button = Button(this)
        button.text = s.trim()
        button.setBackgroundColor(Color.parseColor("#ECC03B"))
        binding.TeacherMenuLayout.addView(button)
        button.setOnClickListener { QuizMenu(button.text.toString()) }
    }
//initializes buttons for all existing quizes
    fun InitializeButtons(){
        var data = db.readQuizNames()
        for(i in 0..data.size - 1){
            InitializeButton(data.get(i))
        }
    }
}
