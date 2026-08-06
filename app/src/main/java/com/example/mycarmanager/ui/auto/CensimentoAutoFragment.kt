package com.example.mycarmanager.ui.auto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.mycarmanager.R
import com.example.mycarmanager.ui.home.HomeFragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class CensimentoAutoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_censimento_auto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI elements here using 'view.findViewById'
        val btnAnnulla = view.findViewById<Button>(R.id.btnAnnulla)
        btnAnnulla.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .addToBackStack(null) // Allows user to hit Back to return to HomeFragment
                .commit()
        }

        // Set up RecyclerView, adapters, etc.
    }
}
