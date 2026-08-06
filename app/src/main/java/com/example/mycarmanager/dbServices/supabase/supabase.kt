package com.example.mycarmanager.dbServices.supabase



import com.example.mycarmanager.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.auth.Auth

object SupabaseInstance {
    //chiave API e URL per connettersi al database Supabase
    private const val URL = "https://zvaddmvzsgakkkwyvocm.supabase.co"
    private const val KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inp2YWRkbXZ6c2dha2trd3l2b2NtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODQ3MDM1MTAsImV4cCI6MjEwMDI3OTUxMH0.OleeaV0tF90PgOUUf1Tt8ui9sMP8SjhVtFAyIvmm6XM"

    //creazione del client Supabase
    val client = createSupabaseClient(URL, KEY) {
        install(Postgrest)
        install(Auth)
    }

}

