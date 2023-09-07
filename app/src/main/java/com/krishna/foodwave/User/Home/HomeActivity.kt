package com.krishna.foodwave.User.Home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.krishna.foodwave.Admin.model.AddProductModel
import com.krishna.foodwave.Admin.model.CategoryModel
import com.krishna.foodwave.R
import com.krishna.foodwave.User.Activity.CartActivity
import com.krishna.foodwave.User.Activity.highRatingProductAdapter
import com.krishna.foodwave.User.Adapter.ProductAdapter
import com.krishna.foodwave.User.Adapter.UserCategoryAdapter
import com.krishna.foodwave.User.LoginActivity.LoginActivity
import com.krishna.foodwave.User.LoginActivity.UserLoginFragment

import com.krishna.foodwave.User.model.AdreesModel
import com.krishna.foodwave.User.model.User
import com.krishna.foodwave.User.mvvm.model.ProductModel
import com.krishna.foodwave.User.ui.AccountActivity
import com.krishna.foodwave.User.ui.SettingsActivity
import com.krishna.foodwave.User.ui.WhishListActivity
import com.krishna.foodwave.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    var currentUserID = FirebaseAuth.getInstance().currentUser?.uid
    val databaseReference = FirebaseDatabase.getInstance().reference
    var i=0;
    private  lateinit var binding:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        FirebaseApp.initializeApp(this)


        catSetupRecyclerView()
        GridSetupRecyclerView()
        ProductSetupRecylerView()
        getSlide()

        binding.Profile.setOnClickListener {
          binding.drawerLayout.open()
        }
        val navigationView = findViewById<NavigationView>(R.id.navigation)
        // Replace with your NavigationView ID
        val headerView = navigationView.getHeaderView(0)
        val headerImageView = headerView.findViewById<ImageView>(R.id.Hdprofile)
        val headerNameTextView = headerView.findViewById<TextView>(R.id.HdName)
        val headerEmailTextView = headerView.findViewById<TextView>(R.id.HdGmail)

        binding.navigation.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.MainHome ->{
                    startActivity(Intent(this,HomeActivity::class.java))
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.whishList -> {
                    startActivity(Intent(this,WhishListActivity::class.java))
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.LogOut ->{
                    var dialog= AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("ok"){dialog,which ->
                            FirebaseAuth.getInstance().signOut()
                            val intent = Intent(this,LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)

                        }.setNegativeButton("Cancel"){dialog, which ->
                            dialog.dismiss()
                        }
                        .create()
                    dialog.show()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }


                R.id.Account ->{
                    startActivity(Intent(this,AccountActivity::class.java))
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.settings ->{
                    startActivity(Intent(this,SettingsActivity::class.java))
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }



        // Set click listener on Profile button

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java))
        }





        if (currentUserID != null) {
            databaseReference.child("Users").child(currentUserID!!).child("Address")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val addressModel = dataSnapshot.getValue(AdreesModel::class.java)
                            val Address = addressModel?.Address ?: "N/A"
                            binding.Address.text = Address

                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                })

        }
        if(currentUserID!=null){
            databaseReference.child("Users").child(currentUserID!!).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val Users = snapshot.getValue(User::class.java)
                        val name = Users?.name ?: "N/A"
                        val img  = Users?.profilePhoto
                        val gmail = Users?.Email
                       binding.Name.text = name
                        Glide.with(this@HomeActivity)
                            .load(img)
                            .into(binding.Profile)

                        headerNameTextView.text = name
                        headerEmailTextView.text = gmail

                        Glide.with(this@HomeActivity)
                            .load(img)
                            .into(headerImageView)
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }




    }







    fun getSlide() {
        Firebase.firestore.collection("slider")
            .document("item")
            .get().addOnSuccessListener {
                val list = it.get("img") as ArrayList<String>
                val Slidelist = ArrayList<SlideModel>()
                for (data in list) {
                    Slidelist.add(SlideModel(data, ScaleTypes.FIT))
                }
                binding.imageSlider.setImageList(Slidelist)
            }

    }



    @SuppressLint("NotifyDataSetChanged")
    fun  ProductSetupRecylerView() {
        val list = ArrayList<AddProductModel>()
        val ProductAdapter = ProductAdapter(this, list)
//        val layoutManger = GridLayoutManager(requireContext(), 2)
//        binding.gridRecyclerView.layoutManager = layoutManger
        binding.ProductRecyclerView.adapter = ProductAdapter

        Firebase.firestore.collection("Products")
            .get()
            .addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(AddProductModel::class.java)
                    data.let {
                        list.add(data!!)
                    }
                    binding.viewtext.visibility = View.VISIBLE
                    ProductAdapter.notifyDataSetChanged()
                }
            }


    }
    @SuppressLint("NotifyDataSetChanged")
    fun GridSetupRecyclerView(){
        val list = ArrayList<AddProductModel>()
        val ProductAdapter = highRatingProductAdapter(this, list)
        val layoutManger = LinearLayoutManager(this)
          binding.gridRecyclerView.layoutManager = layoutManger
         binding.gridRecyclerView.adapter = ProductAdapter

        Firebase.firestore.collection("Products")
            .get()
            .addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(AddProductModel::class.java)
                    data.let {
                        list.add(data!!)
                    }
                    binding.viewtext.visibility = View.VISIBLE
                    ProductAdapter.notifyDataSetChanged()
                }
            }

    }
    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    fun catSetupRecyclerView() {
        val list = ArrayList<CategoryModel>()
        val CategryAdapter = UserCategoryAdapter(this, list)
        binding.CategoryRecycler.adapter = CategryAdapter

        val layoutManger = GridLayoutManager(this,4)
       binding.CategoryRecycler.layoutManager = layoutManger
        Firebase.firestore.collection("Category")
            .get()
            .addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(CategoryModel::class.java)
                    data.let {
                        list.add(data!!)
                    }
                    CategryAdapter.notifyDataSetChanged()

                }
            }


    }

}
