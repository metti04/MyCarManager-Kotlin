package com.example.mycarmanager.dbServices.model

import kotlinx.serialization.Serializable

@Serializable
enum class Alimentazione {
    BENZINA, GASOLIO, ELETTRICA, IBRIDO, GPL, METANO, IDROGENO
}

@Serializable
enum class TipologiaLavoro {
    ORDINARIO, STRAORDINARIO
}

@Serializable
enum class TipologiaGestione {
    POSSESSORE, NON_POSSESSORE
}

@Serializable
enum class StatoLavoro {
    ESEGUITO, DA_ESEGUIRE
}

@Serializable
enum class StatoAuto {
    ATTIVO, INATTIVO
}

@Serializable
enum class StatoObbligo {
    PAGATO, DA_PAGARE
}
