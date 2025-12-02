package com.miraflores.agenda.db
import android.content.ContentValues
import android.content.Context
import com.miraflores.agenda.interfaces.CrudInterface
import com.miraflores.agenda.model.Cita

class CitaDAO(context: Context) : CrudInterface<Cita> {
    private val dbHelper = AdminSQLiteOpenHelper(context)

    override fun insertar(obj: Cita): Long {
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

    override fun obtenerTodos(): ArrayList<Cita> {
        val lista = ArrayList<Cita>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM citas ORDER BY id DESC", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(Cita(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    override fun eliminar(id: Int): Int {
        val db = dbHelper.writableDatabase
        val res = db.delete("citas", "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }
}