package com.example.covidadminside

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.covidadminside.databinding.ActivityAddNewSlotBinding
import com.google.firebase.firestore.FirebaseFirestore


class AddNewSlotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewSlotBinding

    private lateinit var pincode: Array<String>
    private lateinit var arrayAdapter1: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddNewSlotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pincode = resources.getStringArray(R.array.pincode)

        arrayAdapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, pincode)
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.pincodeSelector.setAdapter(arrayAdapter1)



        binding.pincodeSelector.setOnItemClickListener { parent, view, position, id ->
            val selectedOption = parent.getItemAtPosition(position).toString()
        }

        insertdata()
    }

    private fun insertdata() {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        var selectedOption: String? = null

        // Get the selected option from the dropdown (spinner) once it is selected
        binding.pincodeSelector.setOnItemClickListener { parent, view, position, id ->
            selectedOption = parent.getItemAtPosition(position).toString()
        }

        // Trigger Firestore operation on button click
        binding.button.setOnClickListener {
            if (selectedOption != null &&
                binding.CenterNameEditText1.getText().toString().isNotEmpty() &&
                binding.VaccineNameEditText1.getText().toString().isNotEmpty() &&
                binding.AgeLimitEditText1.getText().toString().isNotEmpty() &&
                binding.AvailableSlotEditText1.getText().toString().isNotEmpty() &&
                binding.AddressEditText1.getText().toString().isNotEmpty() &&
                binding.FeesEditText1.getText().toString().isNotEmpty() &&
                binding.OpeningTimeEditText1.getText().toString().isNotEmpty() &&
                binding.ClosingTimeEditText1.getText().toString().isNotEmpty() &&
                binding.pincodeSelector.getText().toString().isNotEmpty())
            {
                // Ensure to collect all the input data from the user
                val add = hashMapOf(
                    "ageLimit" to binding.AgeLimitEditText1.text.toString().toInt(),
                    "availableCapacity" to binding.AvailableSlotEditText1.text.toString().toInt(),
                    "centerAddress" to binding.AddressEditText1.text.toString(),
                    "centerFromTime" to binding.OpeningTimeEditText1.text.toString(),
                    "centerToTime" to binding.ClosingTimeEditText1.text.toString(),
                    "centerName" to binding.CenterNameEditText1.text.toString(),
                    "fee_type" to binding.FeesEditText1.text.toString(),
                    "vaccineName" to binding.VaccineNameEditText1.text.toString()
                )
                // Add data to Firestore under the selected collection
                db.collection(selectedOption!!).document()
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
