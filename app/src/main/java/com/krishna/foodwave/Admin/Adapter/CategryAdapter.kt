package com.krishna.foodwave.Admin.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.krishna.foodwave.Admin.model.CategoryModel
import com.krishna.foodwave.R
import com.krishna.foodwave.databinding.ItemCategoryLayoutBinding

class CategryAdapter( var context: Context, var list : ArrayList<CategoryModel>) :
    RecyclerView.Adapter<CategryAdapter.CatViewHolder>() {
    inner  class CatViewHolder( val binding: ItemCategoryLayoutBinding) : RecyclerView.ViewHolder(binding.root){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
       val binding = ItemCategoryLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return CatViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return  list.size
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        holder.binding.CatItemText.text = list[position].Name
        Glide.with(context)
            .load(list[position].img)
            .into(holder.binding.CatItemImage)




    }

}