package com.krishna.foodwave.User.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.krishna.foodwave.Admin.model.AddProductModel
import com.krishna.foodwave.User.Activity.ProductDetailsActivity
import com.krishna.foodwave.User.mvvm.model.ProductModel
import com.krishna.foodwave.User.mvvm.viewmodel.ProductViewModel
import com.krishna.foodwave.databinding.FoodItemLayoutBinding


class ProductAdapter(var context: Context, var list: ArrayList<AddProductModel>)
    : RecyclerView.Adapter<ProductAdapter.ProductViewHolder> (){
    lateinit var model:ProductViewModel
    private var isFavourite = false
    inner class ProductViewHolder(val binding: FoodItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root){

        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = FoodItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return  ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
        Log.d("ProductAdapter", "ItemCount: ${list.size}")

    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.binding.foodName.text = list[position].productName
        holder.binding.ProductSp.text = list[position].productSp
        holder.binding.Price.text ="₹"+ list[position].productMrp
        Glide.with(context)
            .load(list[position].productCoverImg)
            .into(holder.binding.foodImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id", list[position].productId)
            context.startActivity(intent)
        }

        }

    }




