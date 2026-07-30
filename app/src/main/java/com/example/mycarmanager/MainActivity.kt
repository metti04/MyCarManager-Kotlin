package com.example.mycarmanager

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var isShowingLogin = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // All'avvio carichiamo direttamente il layout di login
        showLoginLayout()

        // Gestione del tasto back: se siamo in registrazione, torna al login
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!isShowingLogin) {
                    showLoginLayout()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun showLoginLayout() {
        isShowingLogin = true
        setContentView(R.layout.activity_login)

        // Listener per passare alla registrazione
        findViewById<TextView>(R.id.tvRegistrati)?.setOnClickListener {
            showRegistrationLayout()
        }

//        // Altri listener del login
//        findViewById<Button>(R.id.btnGoogle)?.setOnClickListener {
//            Toast.makeText(this, "Google Sign In...", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun showRegistrationLayout() {
        isShowingLogin = false
        setContentView(R.layout.activity_registrazione)

        // Listener per tornare al login
//        findViewById<Button>(R.id.registrati)?.setOnClickListener {
//            Toast.makeText(this, "Registrazione completata!", Toast.LENGTH_SHORT).show()
//            showLoginLayout()
//        }
    }

    private fun setupInsets(rootViewId: Int) {
        val root = findViewById<android.view.View>(rootViewId)
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }
}
