package com.example.androidassignment

class QuizDB {

        var quizName: String = ""

        var questionId: Int = 0

        var questionContent: String = ""

        var answerId: Int = 0

        var answerContent: String = ""

        var isCorrect: String = "0"

    //constructor for empty quiz
    constructor(quizName: String) {
        this.quizName = quizName
    }
    //constructor for empty question
    constructor(quizName: String, questionId: Int, questionContent: String) {
        this.quizName = quizName
        this.questionId = questionId
        this.questionContent = questionContent
    }
    //constructor for answer
    constructor(quizName: String, questionId: Int, questionContent: String, answerId: Int, answerContent: String, isCorrect: String){
        this.quizName = quizName
        this.questionId = questionId
        this.questionContent = questionContent
        this.answerId = answerId
        this.answerContent = answerContent
        this.isCorrect = isCorrect

    }


    constructor(){}
}