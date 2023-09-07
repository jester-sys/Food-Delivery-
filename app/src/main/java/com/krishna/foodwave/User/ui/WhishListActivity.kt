package com.krishna.foodwave.User.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.krishna.foodwave.Admin.model.AddProductModel
import com.krishna.foodwave.R
import com.krishna.foodwave.User.Activity.CartActivity
import com.krishna.foodwave.User.Adapter.CartProductAdapter
import com.krishna.foodwave.User.Adapter.WhishListAdapter
import com.krishna.foodwave.User.mvvm.db.ProductDao
import com.krishna.foodwave.User.mvvm.db.ProductDataBase
import com.krishna.foodwave.databinding.ActivityWhishListBinding

class WhishListActivity : AppCompatActivity() {
    var list = ArrayList<AddProductModel>()
    private  lateinit var  list1 :ArrayList<String>
    private  lateinit var  binding:ActivityWhishListBinding
    private lateinit var productDao: ProductDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhishListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list1 = ArrayList()
        productDao = ProductDataBase.getDatabase(this).prodctDao()
        productDao.getProduct().observe(this) {
            binding.whishRecyclerView.adapter = WhishListAdapter(this, it)
            val layoutManger = GridLayoutManager(this, 2)
        binding.whishRecyclerView.layoutManager = layoutManger
            list.clear()
            for (data in it) {
                list1.add(data.productId)
            }
        }}
    }