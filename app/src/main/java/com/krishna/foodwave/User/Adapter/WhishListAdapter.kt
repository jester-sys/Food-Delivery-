package com.krishna.foodwave.User.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.krishna.foodwave.User.Activity.ProductDetailsActivity
import com.krishna.foodwave.User.mvvm.db.ProductDataBase
import com.krishna.foodwave.User.mvvm.model.ProductModel
import com.krishna.foodwave.databinding.CartItemLayoutBinding
import com.krishna.foodwave.databinding.FoodItemLayoutBinding

class WhishListAdapter(val context: Context, val list: List<ProductModel>) : RecyclerView.Adapter<WhishListAdapter.WhishVIewHolder>() {
    inner class  WhishVIewHolder( val binding: FoodItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WhishVIewHolder {
        val binding = FoodItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false)

        return  WhishVIewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: WhishVIewHolder, position: Int) {
        holder.binding.foodName.text = list[position].productName
        holder.binding.ProductSp.text = list[position].productSp
        holder.binding.Price.text = list[position].productMrp
        Glide.with(context)
            .load(list[position].productImages)
            .into(holder.binding.foodImage)


        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id", list[position].productId)
            context.startActivity(intent)

        }



    }
}