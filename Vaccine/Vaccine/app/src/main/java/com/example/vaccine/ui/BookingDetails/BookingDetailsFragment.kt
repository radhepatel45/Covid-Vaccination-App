package com.example.vaccine.ui.BookingDetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covidappdemo1.BookingDetails.BookingDetails
import com.example.covidappdemo1.BookingDetails.BookingDetailsAdapter
import com.example.vaccine.databinding.FragmentBookingDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore

class BookingDetailsFragment : Fragment() {

    private var _binding: FragmentBookingDetailsBinding? = null
    private val binding get() = _binding!!

    private val bookingDetails = ArrayList<BookingDetails>()
    private lateinit var bookingDetailsAdapter: BookingDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        val recyclerView = binding.recyclerView

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        bookingDetailsAdapter = BookingDetailsAdapter(bookingDetails)
        recyclerView.adapter = bookingDetailsAdapter
        recyclerView.setHasFixedSize(true)

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val sharedPreferences = requireContext().getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("USER_EMAIL", null)

        if (userEmail != null) {
            fetchData(userEmail)
        }

        if (userEmail != null) {
            // Fetch data from Firestore
            db.collection("Bookings").document(userEmail)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val vaccineName = document.getString("vaccineName") ?: "Unknown"
                        val hospitalName = document.getString("centerName") ?: "Unknown"
                        val timing = "${document.getString("centerFromTime")} to ${document.getString("centerToTime")}"
                        val feeType = document.getString("fee_type") ?: "Unknown"
                        val location = document.getString("centerAddress") ?: "Unknown"

                        val bookingDetail = BookingDetails(hospitalName, location, feeType, vaccineName, timing)
                        bookingDetails.add(bookingDetail)
                    }
                    bookingDetailsAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to fetch data: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(requireContext(), "User email not found", Toast.LENGTH_SHORT).show()
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
    }
}
