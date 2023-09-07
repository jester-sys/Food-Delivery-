package com.krishna.foodwave.Admin.UI

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.krishna.foodwave.Admin.Adapter.AddProductImageAdapter
import com.krishna.foodwave.R
import com.krishna.foodwave.databinding.FragmentSliderBinding
import java.util.UUID


class SliderFragment : Fragment() {
  private lateinit var binding:FragmentSliderBinding
  private  lateinit var dialog:Dialog
    private  lateinit var listImage: ArrayList<String>
    private  lateinit var list: ArrayList<Uri>
//  private var imageUri: Uri? = null
    private  var launchGeryActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
          val imageUri = it.data!!.data
           list.add(imageUri!!)
            binding.sliderImage.setImageURI(imageUri)

        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSliderBinding.inflate(layoutInflater)
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

        list = ArrayList()
        listImage = ArrayList()



        binding.apply {
            binding.sliderImage.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                launchGeryActivity.launch(intent)

            }
        }
        binding.ulpoadBtn.setOnClickListener {
            if(list.size<1){

                Toast.makeText(requireContext()," Not Image Selected",Toast.LENGTH_LONG).show()

            }
            else{
                dialog.show()
                Toast.makeText(activity,"Image Selected",Toast.LENGTH_SHORT).show()
                uploadImage()


            }
        }

        return binding.root
    }
    private var i=0
    private fun uploadImage() {
        dialog.show()
        val filename = UUID.randomUUID().toString()+".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("Silder/$filename")
        refStorage.putFile(list[i])
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    listImage.add(image!!.toString())
                    if (list.size == listImage.size) {
                        storeData(listImage!!)
                    }
                    else{
                        i++
                        uploadImage()
                    }
                }.addOnFailureListener {
                    dialog.dismiss()
                    Toast.makeText(
                        requireContext(),
                        "Something Wnet Wong with Sotrage",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }}

    private fun storeData(image: ArrayList<String>) {

        var db = Firebase.firestore
        val  data = hashMapOf <String,Any>(
            "img" to image
        )
        db.collection("slider").document("item").set(data)
            .addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(activity,"Slider Uploaded",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(activity,"Slider Not Uploaded",Toast.LENGTH_LONG).show()

            }


    }


}