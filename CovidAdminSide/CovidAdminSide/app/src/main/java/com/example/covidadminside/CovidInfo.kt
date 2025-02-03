package com.example.covidadminside

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.covidadminside.databinding.ActivityAddNewSlotBinding
import com.example.covidadminside.databinding.ActivityCovidInfoBinding
import com.google.firebase.firestore.FirebaseFirestore

class CovidInfo : AppCompatActivity() {

    private lateinit var binding: ActivityCovidInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCovidInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        insertdata()
    }


    private fun insertdata() {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        binding.button.setOnClickListener {
            if (
                binding.CurrentCaseEditText1.getText().toString().isNotEmpty() &&
                binding.VaccinetedEditText1.getText().toString().isNotEmpty() &&
                binding.RecoveredEditText1.getText().toString().isNotEmpty())
            {
                val add = hashMapOf(
                    "CurrentCases" to binding.CurrentCaseEditText1.text.toString().toInt(),
                    "Vaccineted" to binding.VaccinetedEditText1.text.toString().toInt(),
                    "Recovered" to binding.RecoveredEditText1.text.toString().toInt(),
                )
                db.collection("Cases").document("Details")
                    .set(add)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data added", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Data not added", Toast.LENGTH_LONG).show()
                    }

                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter all required fields", Toast.LENGTH_LONG).show()
            }
        }
    }
}