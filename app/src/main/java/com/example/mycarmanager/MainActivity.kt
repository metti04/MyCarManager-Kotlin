package com.example.mycarmanager

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.mycarmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Attiva la modalità edge-to-edge
        enableEdgeToEdge()

        // Effettua l'inflate del binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_login)

        // Gestione degli insets per la navbar di sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0) // Non aggiungiamo padding sotto per la navbar personalizzata
            insets
        }

        // Configura il controller per le icone della barra di stato
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false // icone status bar bianche

        // Carica il fragment iniziale (Garage)
        if (savedInstanceState == null) {
            // Nota: Assicurati che questi Fragment esistano nel tuo progetto
            // Se si chiamano diversamente (es. HomeFragment), cambiali qui
            //replaceFragment(HomeFragment(), "GARAGE")
            updateNavbarUI("GARAGE")
        }

        setupNavigation()
    }

    private fun setupNavigation() {
        binding.llgarage.setOnClickListener {
            //replaceFragment(HomeFragment(), "GARAGE")
            updateNavbarUI("GARAGE")
        }

        binding.llspese.setOnClickListener {
            // Sostituisci con il tuo fragment per le Spese
            //replaceFragment(SpeseFragment(), "SPESE")
            updateNavbarUI("SPESE")
        }

        binding.llscadenze.setOnClickListener {
            // Sostituisci con il tuo fragment per le Scadenze
           //replaceFragment(ScadenzeFragment(), "SCADENZE")
            updateNavbarUI("SCADENZE")
        }

        binding.llprofilo.setOnClickListener {
            // Sostituisci con il tuo fragment per il Profilo
            //replaceFragment(ProfiloFragment(), "PROFILO")
            updateNavbarUI("PROFILO")
        }
    }

    /*private fun replaceFragment(fragment: Fragment, tag: String) {
        val currentFragment = supportFragmentManager.findFragmentByTag(tag)
        if (currentFragment != null && currentFragment.isVisible) return

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
    }*/

    private fun updateNavbarUI(selectedTag: String) {
        val darkBlue = ContextCompat.getColor(this, R.color.Blu_scuro)
        val white = ContextCompat.getColor(this, R.color.Bianco)

        /*// Reset di tutti gli item a Bianco
        resetItem(binding.imgNavMappa, binding.txtNavMappa, white)
        resetItem(binding.imgNavSalvati, binding.txtNavSalvati, white)
        resetItem(binding.imgNavCerca, binding.txtNavCerca, white)
        resetItem(binding.imgNavProfilo, binding.txtNavProfilo, white)

        // Evidenzia il selezionato con Blu Scuro
        when (selectedTag) {
            "GARAGE" -> highlightItem(binding.imgNavMappa, binding.txtNavMappa, darkBlue)
            "SPESE" -> highlightItem(binding.imgNavSalvati, binding.txtNavSalvati, darkBlue)
            "SCADENZE" -> highlightItem(binding.imgNavCerca, binding.txtNavCerca, darkBlue)
            "PROFILO" -> highlightItem(binding.imgNavProfilo, binding.txtNavProfilo, darkBlue)
        }*/
    }

    private fun resetItem(img: ImageView, txt: TextView, color: Int) {
        img.setColorFilter(color)
        txt.setTextColor(color)
    }

    private fun highlightItem(img: ImageView, txt: TextView, color: Int) {
        img.setColorFilter(color)
        txt.setTextColor(color)
    }
}
