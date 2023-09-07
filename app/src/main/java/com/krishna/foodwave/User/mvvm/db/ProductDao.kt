package com.krishna.foodwave.User.mvvm.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.krishna.foodwave.User.mvvm.model.ProductModel

@Dao
interface ProductDao {
    @Query("Select * From  ProductData")
    fun getProduct() : LiveData<List<ProductModel>>
    @Query("SELECT * FROM ProductData WHERE productId= :id ")
    fun isExit(id:String):ProductModel


    @Insert
     suspend fun insertProduct(productModel: ProductModel)

     @Delete
     suspend fun deleteProduct(productModel: ProductModel)
}
