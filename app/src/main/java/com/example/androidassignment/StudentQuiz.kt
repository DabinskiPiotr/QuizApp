package com.example.androidassignment

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.view.allViews
import com.example.androidassignment.databinding.ActivityStudentQuizBinding

class StudentQuiz : AppCompatActivity() {
    lateinit var binding: ActivityStudentQuizBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.QuizName.text = intent.getStringExtra("quizName")
        binding.AllQuestions.text = ("/" + intent.getIntExtra("AllQuestions",1) + "Q")
        binding.CurrentQuestion.text = "1"
        binding.Question.text = ReadQuestionContent(binding.QuizName.text.toString(), intent.getIntArrayExtra("randomOrder")!![binding.CurrentQuestion.text.toString().toInt()-1])
        InitializeButtons()
    }

    val context = this
    var db = DataBaseOperator(context)
    var chosenAnswer: Int? = null
    var score = 0

//reads a content of a question if not found returns Content not found (helped with testing so i left it)
    fun ReadQuestionContent(s: String, qId: Int): String{
        var data = db.readData()
        var content ="Content not found"
        for (i in 0..data.size - 1) {
            if(data.get(i).quizName == s && data.get(i).questionId == qId){
                content = data.get(i).questionContent
            }
        }
        return content
    }
    //Reads an id of a correct answer in a current question
    fun FindCorrectAnswer(): Int{
        var data = db.readData()
        for (i in 0..data.size - 1) {
            if(data.get(i).quizName == binding.QuizName.text.toString() && data.get(i).questionId == intent.getIntArrayExtra("randomOrder")!![binding.CurrentQuestion.text.toString().toInt()-1] && data.get(i).isCorrect == "1"){
                return data.get(i).answerId
            }
        }
        return 0
    }

    //goes to the next question untill the last one, then goes to the score page
    fun NextQuizPage(view: View) {
        if(chosenAnswer == FindCorrectAnswer()){
            score++
        }
        if(binding.CurrentQuestion.text.toString().toInt() < intent.getIntExtra("AllQuestions",1)){
            InitializeWholePage()
        }else{
            startActivity(Intent(this,StudentQuizScore::class.java).putExtra("quizName", binding.QuizName.text).putExtra("score", score).putExtra("AllQuestions",intent.getIntExtra("AllQuestions",1)))
        }
    }


//initializes buttons for a current question in a quiz
    fun InitializeButton(s: String, i: Int) {
        val button: Button = Button(this)
        button.text = s
        button.setBackgroundColor(Color.parseColor("#ECC03B"))
        binding.AnswersHolder.addView(button)
        button.setOnClickListener {ChosenAnswer(i, button)}
    }
//initializes buttons for answers in a question
    fun InitializeButtons() {
        var data = db.readData()
        for (i in 0..data.size - 1) {
            if (data.get(i).quizName == binding.QuizName.text.toString() && data.get(i).questionId == intent.getIntArrayExtra("randomOrder")!![binding.CurrentQuestion.text.toString().toInt()-1] && data.get(i).answerId != 0) {
                InitializeButton(data.get(i).answerContent, data.get(i).answerId)
            }
        }
    }

    //initializes a page with new contents and resets a chosen answer
    fun InitializeWholePage(){
        binding.CurrentQuestion.text = (binding.CurrentQuestion.text.toString().toInt() + 1).toString()
        binding.Question.text = ReadQuestionContent(binding.QuizName.text.toString(), intent.getIntArrayExtra("randomOrder")!![binding.CurrentQuestion.text.toString().toInt()-1])
        binding.AnswersHolder.removeAllViews()
        chosenAnswer = null
        InitializeButtons()
    }
//sets a chosen answer until its changed or it is the next question also changes the collor of a chosen answer
    fun ChosenAnswer(i: Int, b:Button) {
        binding.AnswersHolder.allViews.forEach{view: View -> view.setBackgroundColor(Color.parseColor("#ECC03B"))}
        b.setBackgroundColor(Color.parseColor("#FFBD8801"))
        chosenAnswer = i
    }

}