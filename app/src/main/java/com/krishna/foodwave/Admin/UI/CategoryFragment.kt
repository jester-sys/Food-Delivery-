package com.krishna.foodwave.Admin.UI

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.krishna.foodwave.Admin.Adapter.CategryAdapter
import com.krishna.foodwave.Admin.model.CategoryModel
import com.krishna.foodwave.R
import com.krishna.foodwave.databinding.FragmentCategoryBinding
import java.util.UUID


class CategoryFragment : Fragment() {
    private  lateinit var binding:FragmentCategoryBinding
    private var imageUri: Uri? = null
    private   lateinit var  dialog:Dialog
    private  var launchGeryActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode==Activity.RESULT_OK){
            imageUri = it.data!!.data
            binding.uploadImage.setImageURI(imageUri)
        }

    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)
        setupRecyclerView()

        binding.apply {
            binding.uploadImage.setOnClickListener {
                var intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                launchGeryActivity.launch(intent)
            }
        }

        binding.uploadBtn.setOnClickListener {
            validateData(binding.categoryName.text.toString())
        }
        return  binding.root
    }
    private fun validateData( name: String) {
        if(name.isEmpty()) {
            Toast.makeText(requireContext(), "Please Provide Category Name", Toast.LENGTH_LONG)


            if (imageUri == null) {
                Toast.makeText(requireContext(), "Please Select Image", Toast.LENGTH_LONG).show()
            }
        }

        else {
            uploadImage(name)
        }

    }


    private fun uploadImage( CategoryName: String) {
        dialog.show()
        var fileName  = UUID.randomUUID().toString()+".jpg"
        var storege = FirebaseStorage.getInstance().reference.child("Category/$fileName")
        storege.putFile(imageUri!!).addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {image ->
                storeData(image.toString(),CategoryName)
            }

        }



    }

    private fun storeData(imageUri: String, categoryName: String) {
        var db = Firebase.firestore
        val data = hashMapOf<String,Any>(
            "img" to imageUri,
            "Name" to categoryName
        )
        db.collection("Category").add(data)
            .addOnSuccessListener {
                dialog.dismiss()
                binding.categoryName.text = null
                binding.uploadImage.setImageResource(R.drawable.images)

                Toast.makeText(activity,"Category Uploaded",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(activity,"Category Not Uploaded",Toast.LENGTH_LONG).show()

            }


    }
    fun setupRecyclerView(){
        val list =ArrayList<CategoryModel>()
        var adapter =CategryAdapter(requireContext(),list)
        binding.CategoryRecycler.adapter = adapter
        val layoutManger = LinearLayoutManager(requireContext())
        binding.CategoryRecycler.layoutManager = layoutManger
        Firebase.firestore.collection("Category")
            .get()
            .addOnSuccessListener {
                list.clear()
                for(doc in it.documents){
                    val data = doc.toObject(CategoryModel::class.java)
                    data.let {
                        list.add(data!!)
                    }
                    adapter.notifyDataSetChanged()
                }
            }



    }



}