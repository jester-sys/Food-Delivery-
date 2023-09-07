package com.krishna.foodwave.User.mvvm.db

import androidx.lifecycle.LiveData
import com.krishna.foodwave.User.mvvm.model.ProductModel


class ProductRepository(var productDao: ProductDao) {
    fun getProduct(): LiveData<List<ProductModel>>{
        return productDao.getProduct()
    }
    suspend fun insertProduct(productModel: ProductModel){
        productDao.insertProduct(productModel)
    }
    suspend fun deleteProduct(productModel: ProductModel){
        productDao.deleteProduct(productModel)
    }
}