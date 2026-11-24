package org.proven.diarideviatges.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.proven.diarideviatges.model.RouteEntry
import java.io.File

/**
 * Gestor d'Emmagatzematge Intern Privat (context.filesDir).
 *
 * Les dades es guarden en format JSON dins del directori privat de l'aplicació.
 * Aquest directori és inaccessible per a altres aplicacions i es borra automàticament
 * si l'usuari desinstal·la l'aplicació.
 */
class StorageManager(private val context: Context) {

    private val gson = Gson()
    private val fileName = "diari_viatges.json"

    /**
     - Desa la llista completa de rutes a l'emmagatzematge intern privat (filesDir).
     */
    fun saveRoutes(routes: List<RouteEntry>): Boolean {
        return try {
            val file = File(context.filesDir, fileName)
            val jsonString = gson.toJson(routes)
            file.writeText(jsonString)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     - Carrega la llista de rutes des de l'emmagatzematge intern privat (filesDir).
     - Retorna una llista buida si el fitxer no existeix o hi ha un error.
     */
    fun loadRoutes(): List<RouteEntry> {
        return try {
            val file = File(context.filesDir, fileName)
            if (!file.exists()) {
                return emptyList()
            }
            val jsonString = file.readText()
            if (jsonString.isBlank()) {
                return emptyList()
            }
            val type = object : TypeToken<List<RouteEntry>>() {}.type
            gson.fromJson(jsonString, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
