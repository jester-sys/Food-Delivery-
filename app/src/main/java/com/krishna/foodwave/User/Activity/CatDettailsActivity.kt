package com.krishna.foodwave.User.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat.getCategory
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.krishna.foodwave.Admin.model.AddProductModel
import com.krishna.foodwave.R
import com.krishna.foodwave.User.Adapter.CategoryProductAdapter
import com.krishna.foodwave.User.Adapter.ProductAdapter
import com.krishna.foodwave.databinding.ActivityCatDettailsBinding

class CatDettailsActivity : AppCompatActivity() {
    private  lateinit var  binding:ActivityCatDettailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityCatDettailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getProduct(intent.getStringExtra("Category"))
    }

    private fun getProduct(Category: String?) {
        val list = ArrayList<AddProductModel>()


        Firebase.firestore.collection("Products").whereEqualTo("productCategory", Category)
            .get()
            .addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(AddProductModel::class.java)
                    data.let {
                        list.add(data!!)
                    }
                    binding.CatDetailsRecyclerView.adapter = CategoryProductAdapter(this, list)
                    val layoutManger = GridLayoutManager(this, 2)
                    binding.CatDetailsRecyclerView.layoutManager = layoutManger


                }


            }
    }}