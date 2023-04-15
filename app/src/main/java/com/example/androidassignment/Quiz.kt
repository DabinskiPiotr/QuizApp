package com.example.androidassignment

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.androidassignment.databinding.ActivityQuizBinding



class Quiz : AppCompatActivity() {
    lateinit var binding: ActivityQuizBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.menuTitle.text = intent.getStringExtra("quizName")
        InitializeButtons()
    }
    val context = this
    var db = DataBaseOperator(context)

    fun Back(view: View) {
        startActivity(Intent(this,TeacherMenu::class.java))
    }
    fun AddNewQuestionButton(view: View) {
        binding.PopupAddQuestion.visibility = View.VISIBLE
    }
    //removes a quiz and all its questions and answers and navigates back to the previous activity
    fun DeleteQuiz(view: View) {
        db.deleteQuiz(binding.menuTitle.text.toString())
        startActivity(Intent(this,TeacherMenu::class.java))
    }
//Adds a question and navigates to its menu
    fun AddQuestion(view: View) {
        AddNewQuestion(binding.menuTitle.text.toString(), readQuestionId(binding.menuTitle.text.toString()), binding.NewQuestionContent.text.toString())
        QuestionMenu(readQuestionId(binding.menuTitle.text.toString()) - 1, binding.NewQuestionContent.text.toString())
    }
//function that reads a correct id of a question to display on the buttons
    fun readQuestionId(s :String): Int{
        var counter = 0
        var data = db.readData()
        for (i in 0..data.size - 1) {
            if (data.get(i).quizName == s && data.get(i).answerId == 0) {
                counter++
            }
        }
        return counter
    }

    fun PopupBack(view: View) {
        binding.PopupAddQuestion.visibility = View.INVISIBLE
    }
//initializesa button for question
    fun InitializeButton(s:String,i: Int,content: String) {
        val button: Button = Button(this)
        button.text = s
        button.setBackgroundColor(Color.parseColor("#ECC03B"))
        binding.QuizQuestionsLayout.addView(button)
        button.setOnClickListener { QuestionMenu(i,content)}
    }
// initializes buttons for all existing questions in current quiz
    fun InitializeButtons() {
        var data = db.readData()
        for (i in 0..data.size - 1) {
            if (data.get(i).quizName == binding.menuTitle.text.toString() && data.get(i).questionId != 0 && data.get(i).answerId == 0) {
                InitializeButton("Q" + (data.get(i).questionId).toString() + Dots(data.get(i).questionContent, 25), data.get(i).questionId, data.get(i).questionContent)
            }
        }
    }

    //takes the question content and adds i first characters to the question button
    //so the user can roughly recognise the questions when changing or deleting them
    //adds dots at the end if the question is longer than the i, also returns empty string when question has no content
    fun Dots(s: String, i: Int): String{
        if(s.isNotEmpty()){
            if (s.length > i){
                return " - " + s.take(i).trim() + "..."
            } else {
                return " - " + s
            }
        }else{
            return ""
        }
    }
//adds new question to a current quiz to the database
    fun AddNewQuestion(quizName:String, questionId: Int, questionContent: String){
            var quizDB = QuizDB(quizName, questionId, questionContent)
            db.insertData(quizDB)
    }

    fun QuestionMenu(i:Int,content:String){
        startActivity(Intent(this,Question::class.java).putExtra("quizName",binding.menuTitle.text.toString()).putExtra("questionId",i).putExtra("questionContent",content))
    }

}