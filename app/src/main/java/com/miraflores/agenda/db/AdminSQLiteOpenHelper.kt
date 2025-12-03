package com.miraflores.agenda.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminSQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, "agenda.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        // Creamos la tabla 'citas'
        db.execSQL("CREATE TABLE citas (id INTEGER PRIMARY KEY AUTOINCREMENT, servicio TEXT, paciente TEXT, fecha TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS citas")
        onCreate(db)
    }
}