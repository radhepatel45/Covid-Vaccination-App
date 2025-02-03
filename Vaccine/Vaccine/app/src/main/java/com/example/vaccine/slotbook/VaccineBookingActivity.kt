package com.example.vaccine.slotbook

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covidappdemo1.slotbook.CenterRvModal
import com.example.vaccine.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class VaccineBookingActivity : AppCompatActivity() {

    private lateinit var searchButton: ImageButton
    lateinit var pinCodeEdt: EditText
    lateinit var centersRV: RecyclerView
    lateinit var centerRVAdapter: CenterRVAdapter
    lateinit var centerList: ArrayList<CenterRvModal>
    lateinit var loadingPB: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccine_booking)

        searchButton = findViewById(R.id.idBtnSearch)
        pinCodeEdt = findViewById(R.id.idEdtPinCode)
        centersRV = findViewById(R.id.centersRV)
        loadingPB = findViewById(R.id.idPBLoading)
        centerList = ArrayList()

        centersRV.layoutManager = LinearLayoutManager(this)
        centerRVAdapter = CenterRVAdapter(centerList)
        centersRV.adapter = centerRVAdapter

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val intent = intent
        // receive the value by getStringExtra() method and
        // key must be same which is send by first activity
        val userEmail = intent.getStringExtra("message_key")

        var age : String? = null

        if (userEmail != null) {
            db.collection("Users").document(userEmail)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        age = document.getString("Age")
                    } else {
                        Toast.makeText(this, "No such document", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to fetch data: ${e.message}", Toast.LENGTH_LONG)
                        .show()
                    e.printStackTrace()
                }
        }

        val pinCode = intent.getStringExtra("pincode")
        if (pinCode != null) {
            getAppointments(pinCode)
            pinCodeEdt.setText(pinCode)
        }

        searchButton.setOnClickListener {
            val pinCode = pinCodeEdt.text.toString()
            if (pinCode.length != 6) {
                Toast.makeText(this@VaccineBookingActivity, "Please enter valid pin code", Toast.LENGTH_SHORT)
                    .show()
            } else {
                centerList.clear()

                getAppointments(
                    pinCode,
                )
            }
        }
    }

    private fun getAppointments(pinCode: String) {

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val collectionReference = db.collection(pinCode)

        collectionReference.whereGreaterThanOrEqualTo("ageLimit", 18)
            .get()
            .addOnSuccessListener { documents ->
                loadingPB.visibility = View.GONE
                if (!documents.isEmpty) {
                    for (document in documents) {
                        val center = document.toObject(CenterRvModal::class.java)
                        centerList.add(center)
                    }
                    centerRVAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "No centers found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                loadingPB.visibility = View.GONE
                Toast.makeText(this, "Error getting documents: $exception", Toast.LENGTH_SHORT)
                    .show()
                Log.w("Firestore", "Error getting documents: ", exception)
            }
    }
}
