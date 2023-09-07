package com.krishna.foodwave.User.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.krishna.foodwave.R
import com.krishna.foodwave.User.model.AdreesModel
import com.krishna.foodwave.databinding.ActivityAdressBinding

class AdressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserID != null) {
            val databaseReference = FirebaseDatabase.getInstance().reference

            binding.loginButton.setOnClickListener {
                val address = binding.Address.text.toString().trim()
                val street = binding.Street.text.toString().trim()
                val state = binding.State.text.toString().trim()
                val pinCode = binding.PinCode.text.toString().trim()

                val userAddress = AdreesModel(address, street, state, pinCode)

                databaseReference.child("Users")
                    .child(currentUserID)
                    .child("Address")
                    .setValue(userAddress)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@AdressActivity,
                                "Address Uploaded",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this@AdressActivity,
                                "Failed to upload address",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
    }
}
