package com.example.mycarmanager

import com.example.mycarmanager.ui.home.HomeFragment
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import com.example.mycarmanager.ui.profilo.ProfiloUtenteFragment
import com.example.mycarmanager.ui.scadenze.ScadenzeAutoCensiteFragment
import com.example.mycarmanager.ui.spese.SpeseAutoCensiteFragment

class MainActivity : AppCompatActivity() {

    private var userEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userEmail = intent.getStringExtra("USER_EMAIL")

        // Load HomeFragment by default on launch
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        // Navigation Click Listeners
        findViewById<LinearLayout>(R.id.llgarage).setOnClickListener {
            replaceFragment(HomeFragment())
        }

        findViewById<LinearLayout>(R.id.llspese).setOnClickListener {
            replaceFragment(SpeseAutoCensiteFragment())
        }

        findViewById<LinearLayout>(R.id.llscadenze).setOnClickListener {
            replaceFragment(ScadenzeAutoCensiteFragment())
        }

        findViewById<LinearLayout>(R.id.llprofilo).setOnClickListener {
            val profiloFragment = ProfiloUtenteFragment().apply {
                arguments = Bundle().apply {
                    putString("USER_EMAIL", userEmail)
                }
            }
            replaceFragment(profiloFragment)
        }
    }

    // Helper function to swap fragments programmatically
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}