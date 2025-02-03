package com.example.vaccine.ui.Profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.vaccine.SignInActivity
import com.example.vaccine.databinding.FragmentHomeBinding
import com.example.vaccine.databinding.FragmentProfileBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.jar.Attributes.Name

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        val sharedPreferences = context?.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences?.getString("USER_EMAIL", null)
        if (userEmail != null) {
            fetchData(userEmail)
        }

        binding.Button.setOnClickListener(){
            val intent = Intent(requireContext() , SignInActivity::class.java)
            startActivity(intent)
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
                    val Name = document.getString("Name")
                    val Email = document.getString("Email")
                    val Phone = document.getString("PhoneNum")

                    // Update UI or use the fetched data as needed
                    binding.UserNameTV.text = "User Name: "+ userName
                    binding.UserAgeTV.text = "Age: "+age
                    binding.NameTV.text = "Name: "+Name
                    binding.UserMailTV.text = "Email id: "+Email
                    binding.UserPhonTV.text = "Phone No.: "+Phone

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
