package com.example.androidassignment

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.androidassignment.databinding.ActivityAnswerBinding


class Answer : AppCompatActivity() {
    lateinit var binding: ActivityAnswerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.AnswerId.text = "A" + intent.getIntExtra("answerId",1).toString()
        binding.AnswerContent.text = intent.getStringExtra("answerContent")
    }
    val context = this
    var db = DataBaseOperator(context)

    fun ChangeAnswer(view: View) {
        binding.PopupChangeAnswer.visibility = View.VISIBLE
    }

    fun popupChangeBackButton(view: View) {
        binding.NewAnswerContentInput.text!!.clear()
        binding.PopupChangeAnswer.visibility = View.INVISIBLE
    }
//changes the answer
    fun ChangeAnswerPopupButton(view: View) {
        binding.AnswerContent.text = binding.NewAnswerContentInput.text
        db.updateAnswerContent(intent.getStringExtra("quizName").toString(), intent.getIntExtra("questionId",1), intent.getIntExtra("answerId",1), binding.NewAnswerContentInput.text.toString())
        binding.NewAnswerContentInput.text!!.clear()
        binding.PopupChangeAnswer.visibility = View.INVISIBLE
    }
//deletes an answer, adjusts answers ids and navigates back to the question menu
    fun RemoveAnswer(view: View) {
        db.deleteAnswer(intent.getStringExtra("quizName").toString(), intent.getIntExtra("questionId",1), intent.getIntExtra("answerId",1))
        AdjustAnswersId(intent.getStringExtra("quizName").toString(), intent.getIntExtra("questionId",1), intent.getIntExtra("answerId",1))
        Back(intent.getStringExtra("quizName").toString(),intent.getIntExtra("questionId",1),intent.getStringExtra("questionContent").toString())
    }
//the same function as in question - adjusts the answer ids after deleting one
    fun AdjustAnswersId( s: String, qId: Int, aId: Int){
        var data = db.readData()
        for (i in 0..data.size - 1) {
            if (data.get(i).quizName == s && data.get(i).questionId == qId && aId < data.get(i).answerId){
                db.updateAnswerId(s, qId, data.get(i).answerId)
            }
        }
    }
//sets all other answers to false and sets a current one to true
    fun SetTrue(view: View) {
        var data = db.readData()
        for (i in 0..data.size - 1) {
            if (data.get(i).quizName == intent.getStringExtra("quizName").toString() && data.get(i).questionId == intent.getIntExtra("questionId",1) && data.get(i).answerId != intent.getIntExtra("answerId",1)){
                db.updateAnswerIsCorrect(data.get(i).quizName, data.get(i).questionId, data.get(i).answerId, "0")
            }
        }
        db.updateAnswerIsCorrect(intent.getStringExtra("quizName").toString(), intent.getIntExtra("questionId",1), intent.getIntExtra("answerId",1), "1")
        Toast.makeText(context, "Answer " + intent.getIntExtra("answerId",1).toString() + " is now the correct one", Toast.LENGTH_SHORT).show()
    }

    fun BackButton(view: View) {
        Back(intent.getStringExtra("quizName").toString(),intent.getIntExtra("questionId",1),intent.getStringExtra("questionContent").toString())
    }

    fun Back(s:String, i: Int, content: String){
        startActivity(Intent(this,Question::class.java).putExtra("quizName",s).putExtra("questionId",i).putExtra("questionContent",content))
    }
}