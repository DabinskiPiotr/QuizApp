package com.example.androidassignment

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "QuizDatabaseTest"
private const val TABLE_NAME = "Quiz"

private const val QUIZ_NAME = "QuizName"

private const val QUESTION_ID = "QuestionId"
private const val QUESTION_CONTENT = "QuestionContent"

private const val ANSWER_ID = "AnswerId"
private const val ANSWER_CONTENT = "AnswerContent"
private const val ANSWER_IS_CORRECT = "AnswerIsTrue"

class DataBaseOperator(var context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null,DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE " + TABLE_NAME + " (" + QUIZ_NAME + " TEXT," + QUESTION_ID + " INTEGER," + QUESTION_CONTENT + " TEXT," + ANSWER_ID + " INTEGER," + ANSWER_CONTENT + " TEXT," + ANSWER_IS_CORRECT + " INTEGER)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented")
    }

    fun insertData(quizDB: QuizDB) {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(QUIZ_NAME, quizDB.quizName)
        cv.put(QUESTION_ID, quizDB.questionId)
        cv.put(QUESTION_CONTENT, quizDB.questionContent)
        cv.put(ANSWER_ID, quizDB.answerId)
        cv.put(ANSWER_CONTENT, quizDB.answerContent)
        cv.put(ANSWER_IS_CORRECT, quizDB.isCorrect)
        var result = db.insert(TABLE_NAME, null, cv)
        if (result == -1.toLong())
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Succesfully Added", Toast.LENGTH_SHORT).show()
    }

    fun readData(): List<QuizDB> {
        var list: MutableList<QuizDB> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var quizDB = QuizDB()
                quizDB.quizName = result.getString(result.getColumnIndexOrThrow(QUIZ_NAME))
                quizDB.questionId =
                    result.getString(result.getColumnIndexOrThrow(QUESTION_ID)).toInt()
                quizDB.questionContent =
                    result.getString(result.getColumnIndexOrThrow(QUESTION_CONTENT))
                quizDB.answerId = result.getString(result.getColumnIndexOrThrow(ANSWER_ID)).toInt()
                quizDB.answerContent =
                    result.getString(result.getColumnIndexOrThrow(ANSWER_CONTENT))
                quizDB.isCorrect =
                    result.getString(result.getColumnIndexOrThrow(ANSWER_IS_CORRECT))

                list.add(quizDB)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return list.distinct()
    }


    fun readQuizNames(): List<String> {
        var list: MutableList<String> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                list.add(result.getString(result.getColumnIndexOrThrow(QUIZ_NAME)))
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return list.distinct()
    }


    fun deleteQuiz(s: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, QUIZ_NAME + "=?", arrayOf(s))
        db.close()
    }

    fun deleteQuestion(s: String, i: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, QUIZ_NAME + "=? AND " + QUESTION_ID + "=?", arrayOf(s, i.toString()))
        db.close()
    }

    fun deleteAnswer(s: String, qId: Int , aId: Int){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, QUIZ_NAME + "=? AND " + QUESTION_ID + "=? AND " + ANSWER_ID + "=?", arrayOf(s, qId.toString(), aId.toString()))
        db.close()
    }

    fun updateQuestionId(s: String, i: Int){
        val db = this.writableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var cv = ContentValues()
                cv.put(QUESTION_ID, i - 1)
                db.update(TABLE_NAME, cv, QUIZ_NAME + "=? AND " + QUESTION_ID + "=?", arrayOf(s, i.toString()))
            } while (result.moveToNext())
        }
        result.close()
        db.close()
    }

    fun updateQuestionContent(s: String, i: Int, newQuestion: String) {
        val db = this.writableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var cv = ContentValues()
                cv.put(QUESTION_CONTENT, newQuestion)
                db.update(TABLE_NAME, cv, QUIZ_NAME + "=? AND " + QUESTION_ID + "=?", arrayOf(s, i.toString()))
            } while (result.moveToNext())
        }
        result.close()
        db.close()
    }

    fun updateAnswerId(s: String, qId: Int , aId: Int){
        val db = this.writableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var cv = ContentValues()
                cv.put(ANSWER_ID, aId - 1)
                db.update(TABLE_NAME, cv, QUIZ_NAME + "=? AND " + QUESTION_ID + "=? AND " + ANSWER_ID + "=?", arrayOf(s, qId.toString(), aId.toString()))
            } while (result.moveToNext())
        }
        result.close()
        db.close()
    }

    fun updateAnswerContent(s: String, qId: Int, aId: Int, newAnswer: String){
        val db = this.writableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var cv = ContentValues()
                cv.put(ANSWER_CONTENT, newAnswer)
                db.update(TABLE_NAME, cv, QUIZ_NAME + "=? AND " + QUESTION_ID + "=? AND " + ANSWER_ID + "=?", arrayOf(s, qId.toString(), aId.toString()))
            } while (result.moveToNext())
        }
        result.close()
        db.close()
    }

    fun updateAnswerIsCorrect(s: String, qId: Int, aId: Int, isCorrect: String){
        val db = this.writableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var cv = ContentValues()
                cv.put(ANSWER_IS_CORRECT, isCorrect)
                db.update(TABLE_NAME, cv, QUIZ_NAME + "=? AND " + QUESTION_ID + "=? AND " + ANSWER_ID + "=?", arrayOf(s, qId.toString(), aId.toString()))
            } while (result.moveToNext())
        }
        result.close()
        db.close()
    }

}
