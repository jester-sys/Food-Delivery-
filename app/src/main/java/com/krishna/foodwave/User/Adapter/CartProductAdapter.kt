package com.krishna.foodwave.User.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.krishna.foodwave.User.Activity.CartActivity
import com.krishna.foodwave.User.Activity.ProductDetailsActivity


import com.krishna.foodwave.User.mvvm.db.ProductDataBase
import com.krishna.foodwave.User.mvvm.model.ProductModel
import com.krishna.foodwave.databinding.CartItemLayoutBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class CartProductAdapter(val context: Context, val list: List<ProductModel>)  : RecyclerView.Adapter<CartProductAdapter.CartVIewHolder>(){

    inner class  CartVIewHolder( val binding:CartItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartVIewHolder {
       val binding = CartItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false)

        return  CartVIewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: CartVIewHolder, position: Int) {
        holder.binding.foodName.text = list[position].productName
        holder.binding.price.text = list[position].productMrp
        holder.binding.totalprice.text = list[position].productMrp
        Glide.with(context).load(list[position].productImages).into(holder.binding.imageView)


        val totalpricepref = context.getSharedPreferences("totalPrice", AppCompatActivity.MODE_PRIVATE)
        val countValue = totalpricepref.getInt(list[position].productId, 1)
        val productPrice = (list[position].productMrp as String?)?.toIntOrNull() ?: 0

//

        var totalPrice = 0
        for (product in list) {
            val totalpriceValue = totalpricepref.getInt(product.productId, 0)
            totalPrice += totalpriceValue
        }

        if (countValue <= 1) {
            totalPrice += productPrice
        }

        val gstRate = 0.18
        val deliveryCharges = 10
        val gstAmount = totalPrice * gstRate
        val finalTotalPrice = totalPrice + gstAmount + deliveryCharges

        // Update other UI elements

        CartActivity.binding.subtotal.text = "₹$totalPrice"

        CartActivity.binding.Delivery.text = "₹$deliveryCharges"
       CartActivity.binding.tax.text = "%${gstRate * 100}"
        CartActivity.binding.amount.text = "₹$finalTotalPrice"




        if(countValue>1){
            holder.binding.totalprice.text = countValue.toString()
        }
        else{
            holder.binding.totalprice.text = productPrice.toString()
        }


        val preference = context.getSharedPreferences("CheckOutPrice", AppCompatActivity.MODE_PRIVATE)
        val editor1 = preference.edit() // Replace this with your actual price value
        editor1.putInt("CheckOut", totalPrice) // Use putInt to store an integer value
        editor1.apply()




        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id", list[position].productId)
            context.startActivity(intent)
        }


        val dao = ProductDataBase.getDatabase(context).prodctDao()
        holder.binding.delete.setOnClickListener {
            GlobalScope.launch {
                Dispatchers.IO
                dao.deleteProduct(
                    ProductModel(
                        list[position].productId,
                        list[position].productName,
                        list[position].productMrp,
                        list[position].productImages.toString()
                    )
                )
            }
            val prefEditor = totalpricepref.edit()

            prefEditor.remove(list[position].productId)
            prefEditor.apply()


        }


    }


}








