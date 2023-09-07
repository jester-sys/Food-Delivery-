package com.krishna.foodwave.User.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishna.foodwave.Admin.model.AddProductModel
import com.krishna.foodwave.User.mvvm.db.ProductRepository
import com.krishna.foodwave.User.mvvm.model.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel (private  val productRepository:ProductRepository) : ViewModel(){

    fun getProduct(): LiveData<List<ProductModel>>{
        return productRepository.getProduct()
    }
    fun insertProduct(productModel: ProductModel){
        viewModelScope.launch {
            Dispatchers.IO
            productRepository.insertProduct(productModel)
        }
        fun deleteProduct(productModel: ProductModel){
            viewModelScope.launch {
                Dispatchers.IO
                productRepository.deleteProduct(productModel)
            }
        }
    }
}