package com.miraflores.agenda.interfaces

interface CrudInterface<T> {
    fun insertar(obj: T): Long
    fun obtenerTodos(): List<T>
    fun eliminar(id: Int): Int
}