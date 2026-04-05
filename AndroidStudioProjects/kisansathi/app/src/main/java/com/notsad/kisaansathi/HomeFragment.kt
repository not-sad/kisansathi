package com.notsad.kisansathi.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.notsad.kisansathi.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set welcome name
        val prefs = requireContext().getSharedPreferences("UserAuth", Context.MODE_PRIVATE)
        val name = prefs.getString("name", "Farmer")
        view.findViewById<TextView>(R.id.tvWelcome).text = "Welcome, $name! 👋"

        // Profile icon click
        view.findViewById<View>(R.id.imgProfile).setOnClickListener {
            findNavController().navigate(R.id.nav_profile)
        }

        // Crop recommendation card click
        view.findViewById<CardView>(R.id.cardCropRec).setOnClickListener {
            findNavController().navigate(R.id.nav_crop)
        }
    }
}