package com.example.vaccine

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.vaccine.databinding.ActivityPersonDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Person_details : AppCompatActivity() {

    private lateinit var binding: ActivityPersonDetailsBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        binding = ActivityPersonDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener() {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener() {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()
            val userName = binding.UserNameEditText1.text.toString()
            val name = binding.NameEditText1.text.toString()
            val age = binding.AgeEditText1.text.toString()
            val phoneNum = binding.PhoneNumEditText1.text.toString()


            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty() && binding.UserNameEditText1.getText().toString().isNotEmpty() && binding.NameEditText1.getText().toString().isNotEmpty() && binding.AgeEditText1.getText().toString().isNotEmpty() && binding.PhoneNumEditText1.getText().toString().isNotEmpty() ) {

                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            insertData(email, userName, name, age, phoneNum)
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }

                    }
                } else {
                    Toast.makeText(this, "password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun insertData(email: String, userName: String, name: String, age: String, phoneNum: String) {
        val db = FirebaseFirestore.getInstance()
        val user = hashMapOf(
            "UserName" to userName,
            "Name" to name,
            "Age" to age,
            "PhoneNum" to phoneNum,
            "Email" to email
        )

        db.collection("Users").document(email)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Data added successfully", Toast.LENGTH_LONG).show()
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to add data: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
    }
}