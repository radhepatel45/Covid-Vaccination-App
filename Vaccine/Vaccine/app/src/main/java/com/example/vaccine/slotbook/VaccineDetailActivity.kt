package com.example.vaccine.slotbook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.covidappdemo1.slotbook.CenterRvModal
import com.example.vaccine.MainActivity
import com.example.vaccine.R
import com.example.vaccine.databinding.VaccineDetailActivityBinding
import com.google.firebase.firestore.FirebaseFirestore

class VaccineDetailActivity : AppCompatActivity() {

    private lateinit var vaccineNameTextView: TextView
    private lateinit var centerNameTextView: TextView
    private lateinit var centerAddressTextView: TextView
    private lateinit var centerTimingsTextView: TextView
    private lateinit var ageLimitTextView: TextView
    private lateinit var feeTypeTextView: TextView
    private lateinit var bookSlotButton: Button

    private lateinit var binding: VaccineDetailActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vaccine_detail_activity)

        vaccineNameTextView = findViewById(R.id.vaccineName_text_view)
        centerNameTextView = findViewById(R.id.centerName_text_view)
        centerAddressTextView = findViewById(R.id.centerAddress_text_view)
        centerTimingsTextView = findViewById(R.id.centerTimings_text_view)
        ageLimitTextView = findViewById(R.id.ageLimit_text_view)
        feeTypeTextView = findViewById(R.id.feeType_text_view)
        bookSlotButton = findViewById(R.id.bookSlot_button)

        val center = intent.getParcelableExtra<CenterRvModal>(EXTRA_CENTER)
        center?.let {
            vaccineNameTextView.text = it.vaccineName
            centerNameTextView.text = it.centerName
            centerAddressTextView.text = it.centerAddress
            centerTimingsTextView.text = "From: ${it.centerFromTime} To: ${it.centerToTime}"
            ageLimitTextView.text = "Age Limit: ${it.ageLimit}"
            feeTypeTextView.text = it.fee_type
        }

        val userEmail = intent.getStringExtra("message_Email")

        if (userEmail != null) {
            val db: FirebaseFirestore = FirebaseFirestore.getInstance()

            bookSlotButton.setOnClickListener {
                db.collection("Users").document(userEmail)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            val userName = document.getString("Name")
                            val age = document.getString("Age")

                            center?.let {
                                val add = hashMapOf(
                                    "vaccineName" to it.vaccineName,
                                    "centerName" to it.centerName,
                                    "centerAddress" to it.centerAddress,
                                    "centerFromTime" to it.centerFromTime,
                                    "centerToTime" to it.centerToTime,
                                    "ageLimit" to it.ageLimit,
                                    "fee_type" to it.fee_type,
                                    "UserName" to userName,
                                    "Age" to age
                                )

                                db.collection("Bookings").document(userEmail)
                                    .set(add)
                                    .addOnSuccessListener {
                                        val intent = Intent(this, MainActivity::class.java)
                                        Toast.makeText(this, "Booking added successfully", Toast.LENGTH_LONG).show()
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Failed to add booking", Toast.LENGTH_LONG).show()
                                    }
                            }
                        } else {
                            Toast.makeText(this, "No such document", Toast.LENGTH_LONG).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to fetch user data: ${e.message}", Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
            }
        } else {

            Toast.makeText(this, "User email is missing", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val EXTRA_CENTER = "extra_center"

        fun newIntent(context: Context, center: CenterRvModal): Intent {
            return Intent(context, VaccineDetailActivity::class.java).apply {
                putExtra(EXTRA_CENTER, center)
            }
        }
    }
}
