package com.krishna.foodwave.User.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.krishna.foodwave.R
import com.krishna.foodwave.User.Home.HomeActivity
import com.krishna.foodwave.User.mvvm.db.ProductDao
import com.krishna.foodwave.User.mvvm.db.ProductDataBase
import com.krishna.foodwave.User.mvvm.db.ProductRepository
import com.krishna.foodwave.User.mvvm.model.ProductModel
import com.krishna.foodwave.User.mvvm.viewmodel.ProductViewModel
import com.krishna.foodwave.User.mvvm.viewmodel.ViewModelFactory
import com.krishna.foodwave.databinding.ActivityProductDetailsBinding

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productDao: ProductDao
    lateinit var model: ProductViewModel

    var count = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productDao = ProductDataBase.getDatabase(applicationContext).prodctDao()
        val repository = ProductRepository(productDao)

        productViewModel = ProductViewModel(repository)
        productViewModel = ViewModelProvider(this, ViewModelFactory(repository))[productViewModel::class.java]




        getProductDetails((intent.getStringExtra("id")))


        var preference = getSharedPreferences("totalPrice", MODE_PRIVATE)
        val editor1 = preference.edit()
        editor1.putInt(intent.getStringExtra("id"), intent.getIntExtra("price", 0))
        editor1.apply()



        binding.Increment.setOnClickListener {
            increment(intent.getStringExtra("id"))
        }

        binding.dcrement.setOnClickListener {
            decrement(intent.getStringExtra("id"))

        }


    }

    private fun getProductDetails(ProductId: String?) {
        Firebase.firestore.collection("Products")
            .document(ProductId!!).get().addOnSuccessListener {
                val list = it.get("productImages") as ArrayList<String>
                val name = it.getString("productName")
                val price = it.getString("productMrp")
                val Desc = it.getString("productDescription")


                binding.foodName.text = name
                binding.price.text = price
                binding.totalPrice.text = price
                binding.Desc.text = Desc



                val Slidelist = ArrayList<SlideModel>()
                for (data in list) {
                    Slidelist.add(SlideModel(data, ScaleTypes.FIT))
                }
                binding.imageSlider.setImageList(Slidelist)

                cartAction(ProductId, name, price, it.getString("productCoverImg"))
                FavAction(ProductId,name,price,it.getString("productCoverImg"))


            }.addOnFailureListener {

            }



    }


//    private fun FavAction(productId: String, name: String?, price: String?, sp: String?, img: String?) {
//        lifecycleScope.launch {
//
//            val existingProduct = productDao.isExit(productId)
//
//            withContext(Dispatchers.Main) {
//
//                if (existingProduct != null) {
//                    binding.fav.setImageResource(R.drawable.favorite_icon)
//                    binding.fav.setImageResource(R.drawable.not_favorite_icon)
//                }
//
//                binding.fav.setOnClickListener {
//                    if (existingProduct == null) {
//                        // Add to favorites
//                        AddtoFav(productDao,productId, name, price,sp ,img)
//                        binding.fav.setImageResource(R.drawable.favorite_icon)
//                    } else {
//                        RemoveFromFav(productId)
//                        binding.fav.setImageResource(R.drawable.not_favorite_icon)
//                    }
//                }
//            }
//        }
//    }
private fun FavAction(productId: String, name: String?, price: String?, img: String?) {
    lifecycleScope.launch {
        // Perform background work on the IO dispatcher
        val existingProduct = productDao.isExit(productId)

        // Update the UI on the Main dispatcher
        withContext(Dispatchers.Main) {
            // Set a click listener to toggle favorites
            binding.fav.setOnClickListener {
                if (existingProduct == null) {
                    // Add to favorites
                    AddtoFav(productDao,productId, name, price, img)
                    binding.fav.setImageResource(R.drawable.favorite_icon)
                } else {
                    // Remove from favorites
                    RemoveFromFav(productId)
                    binding.fav.setImageResource(R.drawable.not_favorite_icon)
                }
            }

            // Check if existingProduct is not null and set the initial image resource accordingly
            if (existingProduct != null) {
                binding.fav.setImageResource(R.drawable.favorite_icon) // Product is favorited
            } else {
                binding.fav.setImageResource(R.drawable.not_favorite_icon) // Product is not favorited
            }
        }
    }
}

    private fun AddtoFav(
        productDao: ProductDao,
        productId: String,
        name: String?,
        price: String?,
        img: String?
    ) {
        lifecycleScope.launch {

            Dispatchers.IO
            val existingProduct = productDao.isExit(productId)
            if (existingProduct == null) {
                val data = ProductModel(productId, name, price, img)
//                productDao.insertProduct(data)
                productViewModel.insertProduct(data)
            } else {

                Toast.makeText(this@ProductDetailsActivity, "Product already exists in the Wishlist", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun RemoveFromFav(productId: String) {
        lifecycleScope.launch {
            // Perform database operation on the IO dispatcher
            Dispatchers.IO
            val existingProduct = productDao.isExit(productId)
            if (existingProduct != null) {
                productDao.deleteProduct(existingProduct)
            } else {
                // Product doesn't exist in favorites
                Toast.makeText(this@ProductDetailsActivity, "Product doesn't exist in the Wishlist", Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun cartAction(productId: String, name: String?, price: String?, coverImg: String?) {

        lifecycleScope.launch {
            Dispatchers.IO
            val existingProduct = productDao.isExit(productId)

            withContext(Dispatchers.Main) {

                binding.Cart.text = if (existingProduct != null) {
                    "Go to Cart"

                } else {
                    "Add to Cart"

                }
                binding.Cart.setOnClickListener {
                    if (existingProduct != null) {
                        OpenCart()
                    } else {
                        AddtoCart(productDao, productId, name, price, coverImg)


                    }
                }
            }


        }

    }



    private fun OpenCart() {
        var preference = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", true)
        editor.apply()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()

    }

//    private fun AddtoCart(
//        productDao: ProductDao,
//        productId: String,
//        name: String?,
//        price: String?,
//        coverImg: String?
//    ) {
//        var data = ProductModel(productId, name, price, coverImg)
//        lifecycleScope.launch {
//            Dispatchers.IO
//            productDao.insertProduct(data)
//            binding.Cart.text = " Go to Cart"
//        }
//
//
//    }


    private fun AddtoCart(
        productDao: ProductDao,
        productId: String,
        name: String?,
        price: String?,
        coverImg: String?
    ) {
        lifecycleScope.launch {
            Dispatchers.IO
            val existingProduct = productDao.isExit(productId)
            if (existingProduct == null) {
                val data = ProductModel(productId, name, price,coverImg)
//                productDao.insertProduct(data)
                productViewModel.insertProduct(data)
                binding.Cart.text = "Go to Cart"
            } else {
                Toast.makeText(this@ProductDetailsActivity,"Product already exists in the cart",Toast.LENGTH_LONG).show()

            }
        }
    }




    var newTotalPrice = 0
    private fun increment(ProductId: String?) {

        count++


        val pref = getSharedPreferences("Count", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(ProductId, count)
        editor.apply()

        binding.CountText.text = count.toString()

        val totalPrice = (binding.price.text as String?)?.toIntOrNull() ?: 0
        newTotalPrice = count * totalPrice

        binding.totalPrice.text = newTotalPrice.toString()

        val preference = getSharedPreferences("totalPrice", MODE_PRIVATE)
        val editor1 = preference.edit()
        editor1.putInt(ProductId, newTotalPrice)
        editor1.apply()
    }


    private fun decrement(ProductId: String?) {
        if (count > 1) {
            // Decrement the count value only if it's greater than 1
            count--

            // Update the SharedPreferences with the new count value
            val pref = getSharedPreferences("Count", MODE_PRIVATE)
            val editor = pref.edit()
            editor.putInt(ProductId, count)
            editor.apply()

            // Update the UI with the new count value
            binding.CountText.text = count.toString()

            // Get the product's price from the UI and adjust the multi value
            val totalPrice = (binding.price.text.toString()).toIntOrNull() ?: 0
            newTotalPrice -= totalPrice

            // Update the UI with the new total price
            binding.totalPrice.text = newTotalPrice.toString()

            // Update the SharedPreferences with the new total price
            val preference = getSharedPreferences("totalPrice", MODE_PRIVATE)
            val editor1 = preference.edit()
            editor1.putInt(ProductId, newTotalPrice)
            editor1.apply()
        } else {

        }
    }

}



