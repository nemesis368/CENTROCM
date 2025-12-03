package com.miraflores.agenda.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UsuarioDAO(context: Context) : SQLiteOpenHelper(context, "AgendaDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        // AQUÍ ESTÁ LA CLAVE: Creamos la tabla con las 5 columnas necesarias
        val sql = """
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                telefono TEXT,
                correo TEXT,
                usuario TEXT UNIQUE,
                password TEXT
            )
        """.trimIndent()
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    // Función para guardar TODOS los datos
    fun registrar(nombre: String, telefono: String, correo: String, usuario: String, pass: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("telefono", telefono)
            put("correo", correo)
            put("usuario", usuario)
            put("password", pass)
        }

        // Si devuelve -1 es que hubo error (probablemente usuario repetido)
        val result = db.insert("usuarios", null, values)
        db.close()
        return result != -1L
    }

    fun login(usuario: String, pass: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario=? AND password=?", arrayOf(usuario, pass))
        val existe = cursor.count > 0
        cursor.close()
        db.close()
        return existe
    }
}
