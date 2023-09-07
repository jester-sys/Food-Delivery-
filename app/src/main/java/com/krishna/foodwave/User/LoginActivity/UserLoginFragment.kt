package com.krishna.foodwave.User.LoginActivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.krishna.foodwave.User.Home.HomeActivity
import com.krishna.foodwave.databinding.FragmentUserLoginBinding


class UserLoginFragment : Fragment() {
    lateinit var binding: FragmentUserLoginBinding
    private lateinit var Auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserLoginBinding.inflate(layoutInflater)
        Auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()


            if (email.isNotEmpty() && password.isNotEmpty()) {
                Auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Authentication failed",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            val intent = Intent(activity, HomeActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()

                        }
                    }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enter a valid email and password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.SignIn.setOnClickListener {
            startActivity(Intent(activity, UserSignActivity::class.java))
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        currentUser = Auth.currentUser
        if (currentUser != null) {
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)
        }

    }
}
