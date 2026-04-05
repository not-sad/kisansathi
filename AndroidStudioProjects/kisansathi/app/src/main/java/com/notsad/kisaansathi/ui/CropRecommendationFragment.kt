package com.notsad.kisansathi.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.notsad.kisansathi.R

class CropRecommendationFragment : Fragment() {

    private val cropDatabase = mapOf(
        "Clay" to mapOf(
            "Kharif (Jun-Oct)" to listOf("Rice", "Sugarcane", "Cotton"),
            "Rabi (Nov-Mar)" to listOf("Wheat", "Mustard", "Peas"),
            "Zaid (Mar-Jun)" to listOf("Moong", "Watermelon", "Cucumber")
        ),
        "Sandy" to mapOf(
            "Kharif (Jun-Oct)" to listOf("Groundnut", "Bajra", "Sesame"),
            "Rabi (Nov-Mar)" to listOf("Barley", "Mustard", "Carrot"),
            "Zaid (Mar-Jun)" to listOf("Watermelon", "Muskmelon", "Moong")
        ),
        "Loamy" to mapOf(
            "Kharif (Jun-Oct)" to listOf("Maize", "Soybean", "Cotton", "Rice"),
            "Rabi (Nov-Mar)" to listOf("Wheat", "Gram", "Lentil", "Sunflower"),
            "Zaid (Mar-Jun)" to listOf("Vegetables", "Maize", "Moong")
        ),
        "Black Cotton" to mapOf(
            "Kharif (Jun-Oct)" to listOf("Cotton", "Soybean", "Jowar"),
            "Rabi (Nov-Mar)" to listOf("Wheat", "Gram", "Linseed"),
            "Zaid (Mar-Jun)" to listOf("Sunflower", "Vegetables")
        ),
        "Red" to mapOf(
            "Kharif (Jun-Oct)" to listOf("Groundnut", "Ragi", "Maize"),
            "Rabi (Nov-Mar)" to listOf("Wheat", "Gram", "Mustard"),
            "Zaid (Mar-Jun)" to listOf("Moong", "Cowpea", "Watermelon")
        ),
        "Alluvial" to mapOf(
            "Kharif (Jun-Oct)" to listOf("Rice", "Maize", "Sugarcane", "Cotton"),
            "Rabi (Nov-Mar)" to listOf("Wheat", "Mustard", "Gram", "Peas"),
            "Zaid (Mar-Jun)" to listOf("Vegetables", "Moong", "Watermelon")
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_crop_recommendation, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinnerSoil = view.findViewById<Spinner>(R.id.spinnerCropSoil)
        val spinnerSeason = view.findViewById<Spinner>(R.id.spinnerCropSeason)
        val btnGet = view.findViewById<Button>(R.id.btnGetCrops)
        val tvResults = view.findViewById<TextView>(R.id.tvCropResults)

        val soilTypes = listOf("Select Soil Type", "Clay", "Sandy", "Loamy", "Black Cotton", "Red", "Alluvial")
        spinnerSoil.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, soilTypes)

        val seasons = listOf("Select Season", "Kharif (Jun-Oct)", "Rabi (Nov-Mar)", "Zaid (Mar-Jun)")
        spinnerSeason.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, seasons)

        val prefs = requireContext().getSharedPreferences("FarmerProfile", Context.MODE_PRIVATE)
        val savedSoil = prefs.getString("soilType", "Select Soil Type") ?: "Select Soil Type"
        val savedSeason = prefs.getString("season", "Select Season") ?: "Select Season"
        spinnerSoil.setSelection(soilTypes.indexOf(savedSoil).takeIf { it >= 0 } ?: 0)
        spinnerSeason.setSelection(seasons.indexOf(savedSeason).takeIf { it >= 0 } ?: 0)

        btnGet.setOnClickListener {
            val soil = spinnerSoil.selectedItem.toString()
            val season = spinnerSeason.selectedItem.toString()

            if (soil == "Select Soil Type" || season == "Select Season") {
                tvResults.visibility = View.VISIBLE
                tvResults.text = "⚠️ Please select both Soil Type and Season."
                return@setOnClickListener
            }

            val crops = cropDatabase[soil]?.get(season)
            tvResults.visibility = View.VISIBLE

            if (crops.isNullOrEmpty()) {
                tvResults.text = "No recommendations found."
            } else {
                val result = StringBuilder()
                result.appendLine("✅ Recommended Crops for $soil soil in $season:\n")
                crops.forEachIndexed { index, crop ->
                    result.appendLine("  ${index + 1}. 🌱 $crop")
                }
                result.appendLine("\n💡 Tip: Choose crops suited to your local water availability.")
                tvResults.text = result.toString()
            }
        }
    }
}