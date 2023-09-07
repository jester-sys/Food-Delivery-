package com.krishna.foodwave.Admin.UI

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.krishna.foodwave.databinding.FragmentProductBinding





class ProductFragment : Fragment() {
    private  lateinit var  binding:FragmentProductBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProductBinding.inflate(layoutInflater)

        binding.ProductAddBtn.setOnClickListener {
           startActivity(Intent(activity,AddProductActivity::class.java))
        }
        return  binding.root
    }


}