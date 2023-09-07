package com.krishna.foodwave.User.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.krishna.foodwave.R
import com.krishna.foodwave.User.model.User
import com.krishna.foodwave.databinding.ActivityAccountBinding

class AccountActivity : AppCompatActivity() {
    private  lateinit var  binding:ActivityAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val databasereference  = FirebaseDatabase.getInstance().reference

        if(currentUserID!=null){

            databasereference.child("Users").child(currentUserID).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   if(snapshot.exists()){
                       val User = snapshot.getValue(User::class.java)
                       val  name = User?.name
                       val img = User?.profilePhoto
                       val email = User?.Email
                       Glide.with(this@AccountActivity)
                           .load(img)
                           .into(binding.Profile)
                       binding.username.text = name
                       binding.email.text = email
                   }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}