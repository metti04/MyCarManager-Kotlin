package com.example.mycarmanager.dbServices.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class Lavoro (
    val ID: Int,
    val nome: String,
    val chilometraggio: String,
    val data: LocalDate,
    val descrizione: String,
    val stato: StatoLavoro,
    val costo: Float,
    val pathDocumento: String,
    val intervalloTempo: Int,
    val intervalloKm: Int,
    val targaAuto: String,
)