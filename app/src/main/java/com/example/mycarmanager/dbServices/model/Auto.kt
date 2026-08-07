package com.example.mycarmanager.dbServices.model

import kotlinx.serialization.Serializable

@Serializable
data class Auto(
    val targa: String,
    val modello: String,
    val marchio: String,
    val vin: String,
    val dataimmatricolazione: String,
    val cilindrata: String,
    val alimentazione: Alimentazione,
    val pathLibbretto: String,
    val indetificazioneMotore: String,
    val potenza: Int,
    val stato: StatoAuto,
    val chilometraggio: Int
)



