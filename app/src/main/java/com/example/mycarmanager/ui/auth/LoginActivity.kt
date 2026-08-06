package com.example.mycarmanager.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mycarmanager.MainActivity
import com.example.mycarmanager.R
import com.example.mycarmanager.dbServices.supabase.SupabaseInstance
import com.example.mycarmanager.ui.home.HomeActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.getValue

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                ContextCompat.getColor(this, R.color.Bianco),
                ContextCompat.getColor(this, R.color.Bianco)
            )
        )
        setContentView(R.layout.activity_login)

        // Osserva lo stato del login
        osservaLogin()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clProfiloUtente)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())

            // Sopra blu, sotto 0 (così la card bianca copre tutto il fondo)
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)

            // Il padding per la tastiera lo mettiamo dentro la card bianca
            findViewById<View>(R.id.llLogin).setPadding(
                24,
                24,
                24,
                systemBars.bottom + ime.bottom + 100
            )
            insets
        }

        val webClientId = "983411459011-7mep3okn625fqj5foatnqpet2h5hofru.apps.googleusercontent.com"
        val credentialManager = CredentialManager.create(this)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        // Login con Google
        findViewById<View>(R.id.btnGoogle).setOnClickListener {
            lifecycleScope.launch {
                try {
                    val result = credentialManager.getCredential(this@LoginActivity, request)
                    val credential = result.credential
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    
                    // Passiamo il token al ViewModel
                    viewModel.loginWithGoogle(googleIdTokenCredential.idToken)
                    
                } catch (e: Exception) {
                    Log.e("GoogleAuth", "Errore: ${e.message}")
                    Toast.makeText(this@LoginActivity, "Errore Google: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Login Classico
        findViewById<Button>(R.id.btnAccedi).setOnClickListener {
            val email = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.txEmail).text.toString()
            val pass = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.txPassword).text.toString()
            viewModel.loginUser(email, pass)
        }

        // Navigazione verso la Registrazione
        findViewById<TextView>(R.id.tvRegistrati).setOnClickListener {
            startActivity(Intent(this, RegistrazioneActivity::class.java))
        }
    }

    private fun osservaLogin() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is LoginUiState.Idle -> {}
                        is LoginUiState.Loading -> {
                            Toast.makeText(this@LoginActivity, "Accesso in corso...", Toast.LENGTH_SHORT).show()
                        }
                        is LoginUiState.Success -> {
                            Toast.makeText(this@LoginActivity, "Accesso effettuato!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()
                        }
                        is LoginUiState.Error -> {
                            Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_LONG).show()
                            viewModel.resetState()
                        }
                    }
                }
            }
        }
    }
}
