package com.example.myapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MenuDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "ordering.db"
        private const val DB_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE menu (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                price INTEGER NOT NULL
            )
        """.trimIndent())

        // 預設菜單
        val defaultItems = listOf(
            Pair("漢堡", 100),
            Pair("薯條", 50),
            Pair("可樂", 30),
            Pair("奶茶", 40)
        )
        for (item in defaultItems) {
            db.execSQL("INSERT INTO menu (name, price) VALUES (?, ?)", arrayOf(item.first, item.second))
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS menu")
        onCreate(db)
    }

    fun getAllMenuItems(): List<MenuItem> {
        val list = mutableListOf<MenuItem>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, name, price FROM menu", null)
        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(0)
                    val name = it.getString(1)
                    val price = it.getInt(2)
                    list.add(MenuItem(id, name, price))
                } while (it.moveToNext())
            }
        }
        return list
    }
}
