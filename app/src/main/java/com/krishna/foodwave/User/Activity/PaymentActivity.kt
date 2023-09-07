package com.krishna.foodwave.User.Activity


import android.content.Intent
import android.os.Bundle

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.krishna.foodwave.R
import com.krishna.foodwave.User.Home.HomeActivity
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class PaymentActivity : AppCompatActivity(),PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_NlLjOj92nUuOpL");

        val preference = getSharedPreferences("CheckOutPrice", AppCompatActivity.MODE_PRIVATE)
        val price = preference.getInt("CheckOut", 0) // Use getInt to retrieve an integer value





        try {
            val options = JSONObject()
            options.put("name","FoodWave")
            options.put("description","Demoing Charges")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
            options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("amount",(price*100).toString())//pass amount in currency subunits
            options.put("email","kanhaiyayadav7221@gmail.com")
            options.put("contact","8595788159")
            checkout.open(this,options)
        }catch (e: Exception){
            Toast.makeText(this,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this,"Payment Success: " ,Toast.LENGTH_LONG).show()
//        uploadData()


    }

    private fun uploadData() {
      val id= intent.getStringArrayExtra("ProductID")
        for(currentId in id!!){
            fetechData(currentId)
        }

    }

    private fun fetechData(currentId: String) {

        Firebase.firestore.collection("Products")
            .document(currentId).get().addOnSuccessListener {
                saveData(it.getString("productName"),
                it.getString("productMrp"),
               currentId )
            }
    }

    private fun saveData(name: String?, price: String?, Id: String) {
        val data = hashMapOf<String,Any>()
       data["name"] = name!!
        data["price"] = price!!
        data["ProductID"] = Id!!

        val firestore = Firebase.firestore.collection("AllOrders")
        val key = firestore.document().id
        data["oderId"] = key
        firestore.add(data).addOnSuccessListener {
            Toast.makeText(this,"Oder Placed" , Toast.LENGTH_LONG).show()
            startActivity(Intent(this,HomeActivity::class.java))
            finish()

        }.addOnFailureListener {

        }

    }


    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this,"Error in Error: ",Toast.LENGTH_LONG).show()


    }
}
