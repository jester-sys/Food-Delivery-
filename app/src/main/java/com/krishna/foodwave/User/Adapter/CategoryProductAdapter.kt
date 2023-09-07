package com.krishna.foodwave.User.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.krishna.foodwave.Admin.model.AddProductModel
import com.krishna.foodwave.User.Activity.CatDettailsActivity
import com.krishna.foodwave.User.Activity.ProductDetailsActivity
import com.krishna.foodwave.databinding.FoodItemLayoutBinding
import com.krishna.foodwave.databinding.ItemCategoryProductBinding

class CategoryProductAdapter(var context: Context, var list: ArrayList<AddProductModel>)
    : RecyclerView.Adapter<CategoryProductAdapter.CatProductViewHolder> ()
{
    inner  class CatProductViewHolder(val binding: ItemCategoryProductBinding) :
        RecyclerView.ViewHolder(binding.root){

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatProductViewHolder {
        val binding = ItemCategoryProductBinding.inflate(LayoutInflater.from(context),parent,false)
        return  CatProductViewHolder(binding)

    }

    override fun getItemCount(): Int {
       return  list.size
    }

    override fun onBindViewHolder(holder: CatProductViewHolder, position: Int) {
        holder.binding.foodName.text = list[position].productName
        holder.binding.ProductSp.text = list[position].productSp
        holder.binding.Price.text = list[position].productMrp

        Glide.with(context)
            .load(list[position].productCoverImg)
            .into(holder.binding.foodImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
    }

}}