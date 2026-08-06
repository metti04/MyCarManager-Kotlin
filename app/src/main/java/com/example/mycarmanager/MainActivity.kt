package com.example.mycarmanager

import com.example.mycarmanager.ui.home.HomeFragment
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import com.example.mycarmanager.R
import com.example.mycarmanager.ui.profilo.ProfiloFragment
import com.example.mycarmanager.ui.scadenze.ScadenzeAutoCensiteFragment
import com.example.mycarmanager.ui.spese.SpeseAutoCensiteFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            replaceFragment(ProfiloFragment())
        }
    }

    // Helper function to swap fragments programmatically
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}