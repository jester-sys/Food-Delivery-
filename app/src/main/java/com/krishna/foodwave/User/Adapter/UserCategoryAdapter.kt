package com.krishna.foodwave.User.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.krishna.foodwave.Admin.model.CategoryModel
import com.krishna.foodwave.User.Activity.CatDettailsActivity
import com.krishna.foodwave.databinding.UserCategroyItemLayoutBinding


class UserCategoryAdapter(var context: Context, var list: ArrayList<CategoryModel>) :
RecyclerView.Adapter<UserCategoryAdapter.UserCatViewHolder>(){

    inner  class  UserCatViewHolder( val binding : UserCategroyItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCatViewHolder {
       val binding = UserCategroyItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return  UserCatViewHolder(binding)
    }

    override fun getItemCount(): Int {
    return  list.size
    }

    override fun onBindViewHolder(holder: UserCatViewHolder, position: Int) {
        holder.binding.CatName.text = list[position].Name
        Glide.with(context)
            .load(list[position].img)
            .into(holder.binding.Catimage)
        holder.itemView.setOnClickListener {
            val intent = Intent(context,CatDettailsActivity::class.java)
            intent.putExtra("Category",list[position].Name)
            context.startActivity(intent)
        }
    }

}