package com.notsad.kisansathi.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.notsad.kisansathi.R

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etName = view.findViewById<EditText>(R.id.etName)
        val etLocation = view.findViewById<EditText>(R.id.etLocation)
        val etLandSize = view.findViewById<EditText>(R.id.etLandSize)
        val spinnerSoil = view.findViewById<Spinner>(R.id.spinnerSoilType)
        val spinnerSeason = view.findViewById<Spinner>(R.id.spinnerSeason)
        val etPrevCrop = view.findViewById<EditText>(R.id.etPreviousCrop)
        val btnSave = view.findViewById<Button>(R.id.btnSaveProfile)
        val tvStatus = view.findViewById<TextView>(R.id.tvSaveStatus)

        val soilTypes = listOf("Select Soil Type", "Clay", "Sandy", "Loamy", "Black Cotton", "Red", "Alluvial")
        spinnerSoil.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, soilTypes)

        val seasons = listOf("Select Season", "Kharif (Jun-Oct)", "Rabi (Nov-Mar)", "Zaid (Mar-Jun)")
        spinnerSeason.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, seasons)

        val prefs = requireContext().getSharedPreferences("FarmerProfile", Context.MODE_PRIVATE)
        etName.setText(prefs.getString("name", ""))
        etLocation.setText(prefs.getString("location", ""))
        etLandSize.setText(prefs.getString("landSize", ""))
        etPrevCrop.setText(prefs.getString("prevCrop", ""))
        spinnerSoil.setSelection(soilTypes.indexOf(prefs.getString("soilType", "Select Soil Type")))
        spinnerSeason.setSelection(seasons.indexOf(prefs.getString("season", "Select Season")))

        btnSave.setOnClickListener {
            if (etName.text.isEmpty() || etLocation.text.isEmpty()) {
                tvStatus.text = "⚠️ Please fill Name and Location"
                tvStatus.setTextColor(android.graphics.Color.RED)
                return@setOnClickListener
            }
            prefs.edit().apply {
                putString("name", etName.text.toString())
                putString("location", etLocation.text.toString())
                putString("landSize", etLandSize.text.toString())
                putString("soilType", spinnerSoil.selectedItem.toString())
                putString("season", spinnerSeason.selectedItem.toString())
                putString("prevCrop", etPrevCrop.text.toString())
                apply()
            }
            tvStatus.text = "✅ Profile Saved Successfully!"
            tvStatus.setTextColor(android.graphics.Color.parseColor("#388E3C"))
        }
    }
}