package com.example.mycarmanager.dbServices.model

import java.time.LocalDate

data class Utente (
    val username: String,
    val password: String,
    val email: String,
    val nome: String,
    val cognome: String,
    val dataDiNascita: LocalDate,
)