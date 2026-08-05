package com.example.mycarmanager.dbServices.model

enum class Alimentazione {
    BENZINA, GASOLIO, ELETTRICA, IBRIDO, GPL, METANO, IDROGENO
}

enum class TipologiaLavoro {
    ORDINARIO, STRAORDINARIO
}

enum class TipologiaGestione {
    POSSESSORE, NON_POSSESSORE
}

enum class StatoLavoro {
    ESEGUITO, DA_ESEGUIRE
}

enum class StatoAuto {
    ATTIVO, INATTIVO
}

enum class StatoObbligo {
    PAGATO, DA_PAGARE
}