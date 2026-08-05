package com.example.mycarmanager.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mycarmanager.R
import com.example.mycarmanager.ui.auth.RegistrazioneActivity
import com.example.mycarmanager.ui.auto.CensimentoAutoActivity
import com.example.mycarmanager.ui.schedaAuto.SchedaAutoActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        
        // Se hai un ID per il root layout in activity_home, usalo qui per gli insets
        val root = findViewById<android.view.View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ExtendedFloatingActionButton>(R.id.efabAddVeicolo)?.setOnClickListener {
            val intent = Intent(this, CensimentoAutoActivity::class.java)
            startActivity(intent)
        }

        findViewById<ConstraintLayout>(R.id.clVeicolo)?.setOnClickListener {
            val intent = Intent(this, SchedaAutoActivity::class.java)
            startActivity(intent)
        }
    }
}
