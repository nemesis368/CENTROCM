package com.miraflores.agenda.db

import android.content.ContentValues
import android.content.Context
import com.miraflores.agenda.model.Cita

class CitaDAO(context: Context) {
    private val dbHelper = AdminSQLiteOpenHelper(context)

    fun insertar(obj: Cita): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("servicio", obj.servicio)
            put("paciente", obj.paciente)
            put("fecha", obj.fecha)
        }
        val id = db.insert("citas", null, values)
        db.close()
        return id
    }

    fun obtenerTodos(): ArrayList<Cita> {
        val lista = ArrayList<Cita>()
        val db = dbHelper.readableDatabase
        try {
            val cursor = db.rawQuery("SELECT * FROM citas ORDER BY id DESC", null)
            if (cursor.moveToFirst()) {
                do {
                    lista.add(
                        Cita(
                            id = cursor.getInt(0),
                            servicio = cursor.getString(1),
                            paciente = cursor.getString(2),
                            fecha = cursor.getString(3)
                        )
                    )
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
        return lista
    }
}