package com.miraflores.agenda.db
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class AdminSQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, "MirafloresDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE citas (id INTEGER PRIMARY KEY AUTOINCREMENT, servicio TEXT, paciente TEXT, fecha TEXT)")
        // Aquí podrías agregar tabla usuarios si la usaras con UsuarioDAO
    }
    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        db.execSQL("DROP TABLE IF EXISTS citas")
        onCreate(db)
    }
}
