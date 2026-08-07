package com.example.mycarmanager.dbServices.data

import com.example.mycarmanager.dbServices.model.Utente
import com.example.mycarmanager.dbServices.supabase.SupabaseInstance
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class UtenteDbServices {
    companion object{
        var listaUtenti: List<Utente> = emptyList()
    }
    //creo il client Supabase
    private val client= SupabaseInstance.client

    //funzione di get per prendere un utente singolo
    suspend fun getUtenti(): List<Utente> {
        return client.from("utenti")
            .select { }
            .decodeList<Utente>()
    }
    // funzione di get per prendere piu utenti
    suspend fun getUtente(username: String): Utente? {
        return client.from("utenti")
            .select {
                filter { eq("username", username) }
                order("username", order = Order.DESCENDING)
            }
            .decodeSingleOrNull<Utente>()
    }

    // funzione di get per prendere un utente tramite email
    suspend fun getUtenteByEmail(email: String): Utente? {
        return client.from("utenti")
            .select {
                filter { eq("email", email) }
            }
            .decodeSingleOrNull<Utente>()
    }

    //funzione di inserimento di un utente su db
    suspend fun inserisciUtente(utente: Utente) {
        client.from("utenti").insert(utente)
    }
}

