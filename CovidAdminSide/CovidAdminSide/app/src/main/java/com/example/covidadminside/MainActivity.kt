package com.example.covidadminside

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.covidadminside.BookingDetails.BookingDetailsActivity
import com.example.covidadminside.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addNewSlot.setOnClickListener{
            val intent = Intent(this, AddNewSlotActivity::class.java)
            startActivity(intent)
        }

        binding.bookingDetails.setOnClickListener{
            val intent = Intent(this, BookingDetailsActivity::class.java)
            startActivity(intent)
        }

        binding.AddCovidInformation.setOnClickListener{
            val intent = Intent(this, CovidInfo::class.java)
            startActivity(intent)
        }

    }
}