package com.krishna.foodwave.User.LoginActivity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.krishna.foodwave.User.Home.HomeActivity
import com.krishna.foodwave.databinding.FragmentUserSignBinding
import com.krishna.foodwave.User.model.User

class UserSignActivity : AppCompatActivity() {
    private lateinit var binding: FragmentUserSignBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private var profilePhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentUserSignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                profilePhotoUri = data?.data
                binding.profileImg.setImageURI(profilePhotoUri)
            }
        }

        binding.profileImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            activityResultLauncher.launch(intent)
        }

        binding.loginButton.setOnClickListener {
            val name = binding.Name.text.toString().trim()
            val number = binding.Number.text.toString().trim()
            val email = binding.loginEmail.text.toString().trim()
            val password = binding.loginPassword.text.toString().trim()

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val user = User(
                            name = name,
                            phoneNumber = number,
                            Email = email,
                            password = password,
                            profilePhoto = ""
                        )
                        firebaseDatabase.reference.child("Users").child(userId).setValue(user)
                        uploadProfilePhoto(userId, profilePhotoUri)
                    }
                    Toast.makeText(this, "SignUp Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                }
                try {
                if (task.isSuccessful) {
                    // Sign-up successful logic
                } else {
                    Log.e("SignUpError", "Error: ${task.exception}")
                    Toast.makeText(this, "SignUp Failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("SignUpError", "Exception: $e")
                Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
            }
            }
            }
        }


    private fun uploadProfilePhoto(userId: String, imageUri: Uri?) {
        if (imageUri == null) return

        val imageRef = storage.reference.child("profile_image").child(userId)

        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            Toast.makeText(this, "Profile Photo Uploaded Successfully", Toast.LENGTH_SHORT).show()
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                firebaseDatabase.reference.child("Users").child(userId)
                    .child("profilePhoto")
                    .setValue(uri.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Profile Photo Saved", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Failed to save profile photo", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to upload profile photo", Toast.LENGTH_SHORT).show()
                    }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to upload profile photo", Toast.LENGTH_SHORT).show()
        }
    }
}
