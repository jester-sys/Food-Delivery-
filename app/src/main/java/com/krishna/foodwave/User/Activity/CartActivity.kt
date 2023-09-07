package com.krishna.foodwave.User.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.krishna.foodwave.Admin.model.AddProductModel
import com.krishna.foodwave.User.Adapter.CartProductAdapter

import com.krishna.foodwave.User.model.AdreesModel
import com.krishna.foodwave.User.mvvm.db.ProductDao
import com.krishna.foodwave.User.mvvm.db.ProductDataBase
import com.krishna.foodwave.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {
    var list = ArrayList<AddProductModel>()
    private  lateinit var  list1 :ArrayList<String>
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityCartBinding
    }
    private lateinit var productDao: ProductDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

       binding.AddAddress.setOnClickListener {
           startActivity(Intent(this, AdressActivity::class.java))
        }

        list1 = ArrayList()
        productDao = ProductDataBase.getDatabase(this).prodctDao()

        productDao.getProduct().observe(this) {
            binding.CartRecycler.adapter = CartProductAdapter(this,it)

            list.clear()
            for(data in it){
                list1.add(data.productId)
                binding.OderSummery.visibility = View.VISIBLE
                binding.Info.visibility = View.VISIBLE
                binding.button2.visibility = View.VISIBLE
                binding.textView.visibility = View.VISIBLE
                binding.textView7.visibility = View.VISIBLE
                binding.textView3.visibility = View.INVISIBLE



            }

            binding.button2.setOnClickListener {
                val intent = Intent(this,PaymentActivity::class.java)
                intent.putExtra("ProductID",list)
                this.startActivity(intent)
                val preference =this.getSharedPreferences("CheckOutPrice", AppCompatActivity.MODE_PRIVATE)
                var defaultValue =0
                var Price = preference.getInt("CheckOut",0)


                val editor1 = preference.edit()
                editor1.putInt("CheckOut", Price)
                editor1.apply()


            }

        }


//        var preference =getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
//        val editor = preference.edit()
//        editor.putBoolean("isCart", false)
//        editor.apply()
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val databaseReference = FirebaseDatabase.getInstance().reference

        if (currentUserID != null) {
            databaseReference.child("Users").child(currentUserID).child("Address")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val addressModel = dataSnapshot.getValue(AdreesModel::class.java)
                            val Address = addressModel?.Address?: "N/A"
                            binding.AddAddress.text = Address
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                })
        }



    }
}