package com.miraflores.agenda.dao

import android.content.ContentValues
import android.content.Context
import com.miraflores.agenda.db.AgendaDbHelper
import com.miraflores.agenda.interfaces.ClientDao
import com.miraflores.agenda.model.Client

class ClientDaoImpl(context: Context) : ClientDao {

    private val dbHelper = AgendaDbHelper(context)

    override fun insertClient(client: Client): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            // Ahora sí son accesibles
            put(AgendaDbHelper.COL_NAME, client.name)
            put(AgendaDbHelper.COL_PHONE, client.phone)
            put(AgendaDbHelper.COL_EMAIL, client.email)
        }
        val id = db.insert(AgendaDbHelper.TABLE_CLIENTS, null, values)
        db.close()
        return id
    }

    override fun getAllClients(): ArrayList<Client> {
        val list = ArrayList<Client>()
        val db = dbHelper.readableDatabase

        try {
            val cursor = db.rawQuery("SELECT * FROM ${AgendaDbHelper.TABLE_CLIENTS} ORDER BY ${AgendaDbHelper.COL_ID} DESC", null)

            if (cursor.moveToFirst()) {
                do {
                    // El orden de las columnas depende de cómo se creó en el CREATE TABLE del DbHelper:
                    // 0: ID, 1: NAME, 2: PHONE, 3: EMAIL
                    list.add(Client(
                        id = cursor.getInt(0),
                        name = cursor.getString(1),
                        phone = cursor.getString(2),
                        email = cursor.getString(3)
                    ))
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        db.close()
        return list
    }

    override fun deleteClient(id: Int): Boolean {
        val db = dbHelper.writableDatabase
        val result = db.delete(AgendaDbHelper.TABLE_CLIENTS, "${AgendaDbHelper.COL_ID}=?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }
}