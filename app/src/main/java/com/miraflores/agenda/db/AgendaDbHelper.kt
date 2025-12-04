package com.miraflores.agenda.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.miraflores.agenda.model.Appointment

class AgendaDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "Agenda.db"
        const val DATABASE_VERSION = 3

        // TABLA CITAS (Sin 'private')
        const val TABLE_APPOINTMENTS = "appointments"
        const val COL_ID = "id"
        const val COL_NAME = "client_name"
        const val COL_PHONE = "phone"
        const val COL_AGE = "age"
        const val COL_SERVICE = "service"
        const val COL_DATE = "date"
        const val COL_TIME = "time"
        const val COL_NOTES = "notes"
        const val COL_STATUS = "status"

        // TABLA CLIENTES (Sin 'private')
        const val TABLE_CLIENTS = "clients"
        const val COL_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_APPOINTMENTS (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_NAME TEXT, " +
                "$COL_PHONE TEXT, " +
                "$COL_AGE TEXT, " +
                "$COL_SERVICE TEXT, " +
                "$COL_DATE TEXT, " +
                "$COL_TIME TEXT, " +
                "$COL_NOTES TEXT, " +
                "$COL_STATUS TEXT)")

        db.execSQL("CREATE TABLE $TABLE_CLIENTS (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_NAME TEXT, " +
                "$COL_PHONE TEXT, " +
                "$COL_EMAIL TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_APPOINTMENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTS")
        onCreate(db)
    }

    // --- MÉTODOS CRUD ---
    fun insertAppointment(appointment: Appointment): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_NAME, appointment.clientName)
            put(COL_PHONE, appointment.phone)
            put(COL_AGE, appointment.age)
            put(COL_SERVICE, appointment.service)
            put(COL_DATE, appointment.date)
            put(COL_TIME, appointment.time)
            put(COL_NOTES, appointment.notes)
            put(COL_STATUS, "PENDIENTE")
        }
        return db.insert(TABLE_APPOINTMENTS, null, values)
    }

    fun updateStatus(id: Int, newStatus: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_STATUS, newStatus)
        db.update(TABLE_APPOINTMENTS, values, "$COL_ID=?", arrayOf(id.toString()))
    }

    fun deleteAppointment(id: Int): Int {
        return this.writableDatabase.delete(TABLE_APPOINTMENTS, "$COL_ID=?", arrayOf(id.toString()))
    }

    fun getAppointmentsByService(serviceName: String): List<Appointment> {
        return getListFromQuery("SELECT * FROM $TABLE_APPOINTMENTS WHERE $COL_SERVICE = ? ORDER BY $COL_ID DESC", arrayOf(serviceName))
    }

    fun getAllAppointments(): List<Appointment> {
        return getListFromQuery("SELECT * FROM $TABLE_APPOINTMENTS ORDER BY $COL_ID DESC", null)
    }

    fun updateAppointment(appointment: Appointment): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_NAME, appointment.clientName)
            put(COL_PHONE, appointment.phone)
            put(COL_AGE, appointment.age)
            put(COL_SERVICE, appointment.service)
            put(COL_DATE, appointment.date)
            put(COL_TIME, appointment.time)
            put(COL_NOTES, appointment.notes)
        }
        return db.update(TABLE_APPOINTMENTS, values, "$COL_ID=?", arrayOf(appointment.id.toString()))
    }

    fun getAppointmentById(id: Int): Appointment? {
        val list = getListFromQuery("SELECT * FROM $TABLE_APPOINTMENTS WHERE $COL_ID = ?", arrayOf(id.toString()))
        return if (list.isNotEmpty()) list[0] else null
    }

    private fun getListFromQuery(query: String, args: Array<String>?): List<Appointment> {
        val list = ArrayList<Appointment>()
        val db = this.readableDatabase
        try {
            val cursor = db.rawQuery(query, args)
            if (cursor.moveToFirst()) {
                do {
                    val idxStatus = cursor.getColumnIndex(COL_STATUS)
                    val statusVal = if (idxStatus != -1) cursor.getString(idxStatus) else "PENDIENTE"

                    list.add(Appointment(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        clientName = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                        phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)) ?: "",
                        age = cursor.getString(cursor.getColumnIndexOrThrow(COL_AGE)) ?: "",
                        service = cursor.getString(cursor.getColumnIndexOrThrow(COL_SERVICE)) ?: "",
                        date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)),
                        time = cursor.getString(cursor.getColumnIndexOrThrow(COL_TIME)),
                        notes = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTES)),
                        status = statusVal
                    ))
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: Exception) { e.printStackTrace() }
        return list
    }
}