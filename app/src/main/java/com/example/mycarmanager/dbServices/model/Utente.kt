package com.example.mycarmanager.dbServices.model

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Utente (
    val username: String,
    val password: String,
    val email: String,
    val nome: String,
    val cognome: String,
    val dataDiNascita: LocalDate,
)