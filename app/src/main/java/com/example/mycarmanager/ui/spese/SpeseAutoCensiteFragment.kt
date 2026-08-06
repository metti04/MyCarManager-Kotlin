package com.example.mycarmanager.ui.spese

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mycarmanager.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class SpeseAutoCensiteFragment : Fragment() {

    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_spesa_auto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the XML layout for this fragment
        return inflater.inflate(R.layout.fragment_spesa_auto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI elements here using 'view.findViewById'
        //val fabAddVeicolo = view.findViewById<ExtendedFloatingActionButton>(R.id.efabAddVeicolo)
        //fabAddVeicolo.setOnClickListener {
            // Action for adding vehicle
        //}

        // Set up RecyclerView, adapters, etc.
    }
}
