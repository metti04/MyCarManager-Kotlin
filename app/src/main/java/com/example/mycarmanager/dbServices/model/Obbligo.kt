package com.example.mycarmanager.dbServices.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class Obbligo (
    val ID: Int,
    val nome: String,
    val dataPagamento: LocalDate,
    val costo: Float,
    val dataScadenza: LocalDate,
    val pathDocumento: String,
    val stato: StatoObbligo,
    val targaAuto: String,

)