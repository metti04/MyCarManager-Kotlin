package com.example.mycarmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Applichiamo i padding per le barre di sistema
//        val layout = view.findViewById<View>(R.id.clLogin)
//        if (layout != null) {
//            ViewCompat.setOnApplyWindowInsetsListener(layout) { v, insets ->
//                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//                insets
//            }
//        }

        view.findViewById<Button>(R.id.btnGoogle)?.setOnClickListener {
            onGoogleSignInClick()
        }

        view.findViewById<TextView>(R.id.tvRegistrati)?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.clregistrazione, RegistrazioneFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun onGoogleSignInClick() {
        val credentialManager = CredentialManager.create(requireContext())

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("YOUR_WEB_CLIENT_ID")
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = credentialManager.getCredential(requireActivity(), request)
                Toast.makeText(requireContext(), "Accesso riuscito!", Toast.LENGTH_SHORT).show()
                // Qui potresti mostrare la bottom bar nel MainActivity
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Errore: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
