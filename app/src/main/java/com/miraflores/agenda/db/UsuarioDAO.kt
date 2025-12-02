package com.miraflores.agenda.db

import android.content.ContentValues
import android.content.Context

class UsuarioDAO(context: Context) {
    private val dbHelper = AdminSQLiteOpenHelper(context)

    fun registrar(nombre: String, tel: String, correo: String, usuario: String, pass: String): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            // Asegúrate que tu AdminSQLiteOpenHelper tenga estos campos en la tabla usuarios.
            // Si solo pusiste nombre, usuario y pass en el Helper anterior, borra tel y correo de aquí.
            put("usuario", usuario)
            put("password", pass)
            put("rol", "cliente")
        }
        val res = db.insert("usuarios", null, values)
        db.close()
        return res != -1L
    }

    // Método extra para validar login
    fun login(usuario: String, pass: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario=? AND password=?", arrayOf(usuario, pass))
        val existe = cursor.moveToFirst()
        cursor.close()
        db.close()
        return existe
    }
}
