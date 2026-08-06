package com.example.mycarmanager.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mycarmanager.R
import com.example.mycarmanager.ui.auto.CensimentoAutoActivity
import com.example.mycarmanager.ui.profilo.ProfiloUtenteActivity
import com.example.mycarmanager.ui.scadenze.ScadenzeAutoCensiteActivity
import com.example.mycarmanager.ui.spese.SpeseAutoCensiteActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        
        // Insets per Edge-to-Edge
        val root = findViewById<View>(R.id.clHome)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupNavbar()
        setupFab()
        osservaDati()
    }

    private fun setupNavbar() {
        // La navbar è inclusa con ID "nb" in activity_home.xml
        val navbar = findViewById<View>(R.id.nb)
        
        // Colleghiamo i click ai vari layout della navbar
        navbar.findViewById<LinearLayout>(R.id.llgarage).setOnClickListener {
            // Siamo già in Home (Garage), non facciamo nulla
        }

        navbar.findViewById<LinearLayout>(R.id.llspese).setOnClickListener {
            startActivity(Intent(this, SpeseAutoCensiteActivity::class.java))
        }

        navbar.findViewById<LinearLayout>(R.id.llscadenze).setOnClickListener {
            startActivity(Intent(this, ScadenzeAutoCensiteActivity::class.java))
        }

        navbar.findViewById<LinearLayout>(R.id.llprofilo).setOnClickListener {
            startActivity(Intent(this, ProfiloUtenteActivity::class.java))
        }
    }

    private fun setupFab() {
        findViewById<ExtendedFloatingActionButton>(R.id.efabAddVeicolo)?.setOnClickListener {
            startActivity(Intent(this, CensimentoAutoActivity::class.java))
        }
    }

    private fun osservaDati() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.username.collect { name ->
                    findViewById<TextView>(R.id.tvBenvenuto).text = "Benvenuto $name"
                }
            }
        }
    }
}
