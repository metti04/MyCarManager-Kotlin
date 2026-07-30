package com.example.mycarmanager

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login) // Mostriamo il layout di login
        
        // Applichiamo i padding per le barre di sistema usando l'ID della root del layout login
        val rootLayout = findViewById<android.view.View>(R.id.clLogin)
        if (rootLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        val btnGoogle = findViewById<Button>(R.id.btnGoogle)
        btnGoogle?.setOnClickListener {
            onGoogleSignInClick()
        }
    }

    private fun onGoogleSignInClick() {
        val credentialManager = CredentialManager.create(this)

        // IMPORTANTE: Dovrai sostituire "YOUR_WEB_CLIENT_ID" con il tuo Client ID reale 
        // ottenuto dalla Google Cloud Console (Tipo: Applicazione Web)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("YOUR_WEB_CLIENT_ID")
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = credentialManager.getCredential(this@MainActivity, request)
                // Qui puoi gestire il successo dell'autenticazione
                Toast.makeText(this@MainActivity, "Accesso riuscito!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "Errore durante l'accesso: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
