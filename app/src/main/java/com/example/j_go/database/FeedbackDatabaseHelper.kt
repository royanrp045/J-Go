package com.example.j_go.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FeedbackDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "feedbacks.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_FEEDBACK = "feedback"
        private const val COLUMN_ID = "id"
        private const val COLUMN_PLACE_NAME = "place_name"
        private const val COLUMN_FEEDBACK = "feedback"
        private const val COLUMN_USER_ID = "user_id" // Kolom baru untuk menyimpan user_id
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_FEEDBACK (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_PLACE_NAME TEXT," +
                "$COLUMN_FEEDBACK TEXT," +
                "$COLUMN_USER_ID TEXT)" // Menambahkan user_id ke dalam tabel
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FEEDBACK")
        onCreate(db)
    }

    fun addFeedback(placeName: String, feedback: String, userId: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_PLACE_NAME, placeName)
        values.put(COLUMN_FEEDBACK, feedback)
        values.put(COLUMN_USER_ID, userId) // Menyimpan user_id saat menambahkan feedback
        db.insert(TABLE_FEEDBACK, null, values)
        db.close()
    }

    fun getAllFeedbacksForUser(userId: String): List<Feedback> {
        val feedbackList = mutableListOf<Feedback>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_FEEDBACK,
            null,
            "$COLUMN_USER_ID = ?",  // Filter berdasarkan user_id
            arrayOf(userId),
            null, null, null
        )
        if (cursor.moveToFirst()) {
            do {
                val placeNameIndex = cursor.getColumnIndex(COLUMN_PLACE_NAME)
                val feedbackIndex = cursor.getColumnIndex(COLUMN_FEEDBACK)

                if (placeNameIndex != -1 && feedbackIndex != -1) {
                    val placeName = cursor.getString(placeNameIndex)
                    val feedback = cursor.getString(feedbackIndex)
                    feedbackList.add(Feedback(placeName, feedback))
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return feedbackList
    }
}

data class Feedback(val placeName: String, val feedback: String)
