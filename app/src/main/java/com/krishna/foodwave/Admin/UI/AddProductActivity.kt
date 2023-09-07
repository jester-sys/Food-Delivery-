package com.krishna.foodwave.Admin.UI

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.krishna.foodwave.Admin.Adapter.AddProductImageAdapter
import com.krishna.foodwave.Admin.model.AddProductModel
import com.krishna.foodwave.Admin.model.CategoryModel
import com.krishna.foodwave.R
import com.krishna.foodwave.databinding.ActivityAddProductBinding

import java.util.UUID

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private  lateinit var list: ArrayList<Uri>
    private  lateinit var listImage: ArrayList<String>
    private lateinit   var Producteradapter: AddProductImageAdapter
    private  var coverImge:Uri? = null
    private lateinit var dialog: Dialog
    private  var coverImageUrl:String = ""
    private lateinit var categoryList :ArrayList<String>


    @SuppressLint("NotifyDataSetChanged")
    private  var launchGeryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()

    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            coverImge = it.data!!.data
            binding.producCoverImg.setImageURI(coverImge)
            binding.producCoverImg.visibility = View.VISIBLE

        }
    }
    private  var launchProductActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val imageUrl = it.data!!.data
            list.add(imageUrl!!)
            Producteradapter.notifyDataSetChanged()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)

        setContentView(binding.root)

        list = ArrayList()
        listImage = ArrayList()
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

        setProductCategory()

        binding.selectCoverImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            launchGeryActivity.launch(intent)

        }
        binding.productImgBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            launchProductActivity.launch(intent)


        }





         Producteradapter = AddProductImageAdapter(list)
        binding.productImgRecyclerView.adapter = Producteradapter

        binding.submitProductBtn.setOnClickListener {
            validateData()
        }


    }
    private fun validateData() {
        if(binding.ProductNameEdt.text.toString().isEmpty()){
            binding.ProductNameEdt.requestFocus()
            binding.ProductNameEdt.error = "Empty"
        }
        else if(binding.ProductDescEdt.text.toString().isEmpty()){
            binding.ProductDescEdt.requestFocus()
            binding.ProductDescEdt.error = "Empty"
        }
        if(binding.ProductMRPEdt.text.toString().isEmpty()){
            binding.ProductMRPEdt.requestFocus()
            binding.ProductMRPEdt.error = "Empty"
        }
        if(binding.ProductSpEdt.text.toString().isEmpty()){
            binding.ProductSpEdt.requestFocus()
            binding.ProductSpEdt.error = "Empty"
        }
        else if(coverImge==null){
            Toast.makeText(this,"Please Select CoverImage",Toast.LENGTH_LONG).show()
        }
        else if(list.size <1){
            Toast.makeText(this,"Please Slect The Product Image",Toast.LENGTH_LONG).show()
        }
        else{
            uploadImage()
        }

    }

    private fun uploadImage() {
        dialog.show()
        val filename = UUID.randomUUID().toString()+".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("Products/$filename")
        refStorage.putFile(coverImge!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {image ->
                    coverImageUrl = image.toString()
                    uploadProductImage()
                }
            }.addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(this,"Something Wnet Wong with Sotrage", Toast.LENGTH_LONG).show()
            }

    }

    private var i=0
    private fun uploadProductImage() {
        val filename = UUID.randomUUID().toString()+".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("Products/$filename")
        refStorage.putFile(list[i])
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {image ->
                    listImage.add(image!!.toString())
                    if(list.size== listImage.size){
                        storeData()
                    }
                    else{
                        i++
                        uploadProductImage()
                    }

                }
            }.addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(this,"Something Wnet Wong with Sotrage",Toast.LENGTH_LONG).show()
            }

    }

    private fun storeData() {
        if (categoryList.isNotEmpty() && binding.ProductCategoryDropDown.selectedItemPosition >= 0) {
            val db = Firebase.firestore.collection("Products")
            val key = db.document().id
            val data = AddProductModel(
                binding.ProductNameEdt.text.toString(),
                binding.ProductDescEdt.text.toString(),
                coverImageUrl,
                categoryList[binding.ProductCategoryDropDown.selectedItemPosition],
                key,
                binding.ProductMRPEdt.text.toString(),
                binding.ProductSpEdt.text.toString(),
                listImage
            )
            db.document(key).set(data).addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(this, "Product Added", Toast.LENGTH_LONG).show()
                binding.ProductNameEdt.text = null
            }
                .addOnFailureListener{
                    Toast.makeText(this, "Product not Added", Toast.LENGTH_LONG).show()
                }
        } else {
            // Handle the case where categoryList is empty or no category is selected.
            Toast.makeText(this, "Please select a category", Toast.LENGTH_LONG).show()
        }
    }

    fun setProductCategory() {
        categoryList = ArrayList()
        Firebase.firestore.collection("Category").get().addOnSuccessListener {
            categoryList.clear()
            for (doc in it.documents) {
                val data = doc.toObject(CategoryModel::class.java)
                categoryList.add(data?.Name!!)
            }
            categoryList.add(0, "Select Category")

            // Debug: Log the categoryList to see its content
            Log.d("CategoryList", categoryList.toString())

            val arrAdapter = ArrayAdapter(this, R.layout.dropdown_item_layout, categoryList)
            binding.ProductCategoryDropDown.adapter = arrAdapter
        }
    }

}