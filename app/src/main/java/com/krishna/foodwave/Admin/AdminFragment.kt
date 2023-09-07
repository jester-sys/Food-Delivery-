package com.krishna.foodwave.Admin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.krishna.foodwave.Admin.UI.AdminHomeActivity

import com.krishna.foodwave.R
import com.krishna.foodwave.databinding.FragmentAdminBinding


class AdminFragment : Fragment() {
    private  lateinit var binding:FragmentAdminBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminBinding.inflate(layoutInflater)
        binding.loginButton.setOnClickListener {
            startActivity(Intent(activity,AdminHomeActivity::class.java))
        }
        return  binding.root
    }


}