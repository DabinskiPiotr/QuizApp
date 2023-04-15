package com.example.androidassignment

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.androidassignment.databinding.ActivityQuestionBinding

class Question : AppCompatActivity() {
    lateinit var binding: ActivityQuestionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.QuestionId.text = "Q" + intent.getIntExtra("questionId",1).toString()
        binding.QuestionContent.text = intent.getStringExtra("questionContent")
        InitializeButtons()
    }
    val context = this
    var db = DataBaseOperator(context)

    fun BackButton(view: View) {
        Back()
    }

    fun AddNewAnswer(view: View) {
        binding.PopupQuestionMenu.visibility = View.VISIBLE
    }
//If there is less than 10 answers, adds new one
    fun CreateNewAnswer(view: View) {
        if(readAnswerId(intent.getStringExtra("quizName").toString(), intent.getIntExtra("questionId",1)) < 11){
        AddNewAnswer(intent.getStringExtra("quizName").toString(), intent.getIntExtra("questionId",1), binding.QuestionContent.text.toString(), readAnswerId(intent.getStringExtra("quizName").toString(), intent.getIntExtra("questionId",1)), binding.NewAnswerContentInput.text.toString(), "0")
        AnswerMenu(readAnswerId(intent.getStringExtra("quizName").toString(), intent.getIntExtra("questionId",1)) - 1, binding.NewAnswerContentInput.text.toString(), "0")
    }else{
            Toast.makeText(context, "Failed - Max 10", Toast.LENGTH_SHORT).show()
        }
    }
    //function that reads a correct id of an answer to display on the buttons
    fun readAnswerId(s :String, int :Int): Int{
        var counter = 0
        var data = db.readData()
        for (i in 0..data.size - 1) {
            if (data.get(i).quizName == s && data.get(i).questionId == int) {
                counter++
            }
        }
        return counter
    }

    fun popupBackButton(view: View) {
        binding.PopupQuestionMenu.visibility = View.INVISIBLE
    }
//removes the question and its answers, adjusts all higher id questionIds to one less and goes back to the quiz menu
    fun RemoveQuestion(view: View) {
        db.deleteQuestion(intent.getStringExtra("quizName").toString(), intent.getIntExtra("questionId",1))
        AdjustQuestionsId(intent.getStringExtra("quizName").toString(), intent.getIntExtra("questionId",1))
        Back()
    }
//adjusts an id of all higher id questions to compensate for removing a quesstions
    fun AdjustQuestionsId( s: String, qId: Int){
        var data = db.readData()
        for (i in 0..data.size - 1) {
            if (data.get(i).quizName == s && qId < data.get(i).questionId) {
                db.updateQuestionId(s, data.get(i).questionId)
            }
        }
    }

    fun ChangeQuestion(view: View) {
        binding.PopupChangeQuestion.visibility = View.VISIBLE
    }
//changes a question
    fun ChangeQuestionPopupButton(view: View) {
        binding.QuestionContent.text = binding.NewQuestionContentInput.text
        db.updateQuestionContent(intent.getStringExtra("quizName").toString(), intent.getIntExtra("questionId",1), binding.NewQuestionContentInput.text.toString())
        binding.NewQuestionContentInput.text!!.clear()
        binding.PopupChangeQuestion.visibility = View.INVISIBLE
    }

    fun popupChangeBackButton(view: View) {
        binding.NewQuestionContentInput.text!!.clear()
        binding.PopupChangeQuestion.visibility = View.INVISIBLE
    }


    fun Back(){
        startActivity(Intent(this,Quiz::class.java).putExtra("quizName",intent.getStringExtra("quizName")))
    }
//adds new answer to a question
    fun AddNewAnswer(quizName: String, questionId: Int, questionContent: String, answerId: Int, answerContent: String, isCorrect: String){
        var quizDB = QuizDB(quizName, questionId, questionContent, answerId, answerContent, isCorrect)
        db.insertData(quizDB)
    }

    fun AnswerMenu(id: Int, content: String, isCorrect: String){
        startActivity(Intent(this,Answer::class.java).putExtra("quizName",  intent.getStringExtra("quizName").toString()).putExtra("questionId", intent.getIntExtra("questionId", 1)).putExtra("questionContent", binding.QuestionContent.text).putExtra("answerId",id).putExtra("answerContent", content).putExtra("isCorrect", isCorrect))
    }
//Initializes a button and depending if the answer is true or false changes it's collor
    fun InitializeButton(s: String, i: Int, content: String, isCorrect: String) {
        val button: Button = Button(this)
        button.text = s
        if(isCorrect.equals("1")){
            button.setBackgroundColor(Color.parseColor("#FF34B525"))
        }else{
            button.setBackgroundColor(Color.parseColor("#FFFF452D"))
        }
        binding.QuestionMenuLinear.addView(button)
        button.setOnClickListener { AnswerMenu(i, content, isCorrect)}
    }
//initializes all buttons for the answers
    fun InitializeButtons() {
        var data = db.readData()
        for (i in 0..data.size - 1) {
            if (data.get(i).quizName == intent.getStringExtra("quizName").toString() && data.get(i).questionId == intent.getIntExtra("questionId",1) && data.get(i).answerId != 0) {
                InitializeButton("A" + (data.get(i).answerId).toString() + Dots(data.get(i).answerContent, 25) , data.get(i).answerId, data.get(i).answerContent, data.get(i).isCorrect)
            }
        }
    }

    //the same function from Quiz it gives a hint to the answers in the menu
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

}