package com.example.vaccine.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vaccine.slotbook.VaccineBookingActivity
import com.example.vaccine.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        val sharedPreferences = context?.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences?.getString("USER_EMAIL", null)
        if (userEmail != null) {
            fetchData(userEmail)
        }

        binding.searchButton.setOnClickListener {
            val pincode = binding.pincodeEditText.text.toString()
            if (pincode.length != 6) {
                Toast.makeText(requireContext(), "Please enter valid pin code", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val intent = Intent(requireContext(), VaccineBookingActivity::class.java)
                intent.putExtra("pincode" , pincode)
                startActivity(intent)
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchData(userName: String) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        db.collection("Users").document(userName)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val userName = document.getString("UserName")
                    val age = document.getString("Age")
                    // Update UI or use the fetched data as needed
                    binding.UserNameTV.text = "Name: "+ userName
                    binding.UserAgeTV.text = "Age: "+age
                } else {
                    Toast.makeText(requireContext(), "No such document", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to fetch data: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }

        db.collection("Cases").document("Details")
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val currentCases = document.get("CurrentCases")?.toString() ?: "N/A"
                    val recovered = document.get("Recovered")?.toString() ?: "N/A"
                    val vaccinated = document.get("Vaccineted")?.toString() ?: "N/A"

                    // Update UI or use the fetched data as needed
                    binding.currentCasesValue.text = currentCases
                    binding.vaccinatedValue.text = vaccinated
                    binding.recoveredValue.text = recovered

                } else {
                    Toast.makeText(requireContext(), "No such document", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to fetch data: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }

    }
}
