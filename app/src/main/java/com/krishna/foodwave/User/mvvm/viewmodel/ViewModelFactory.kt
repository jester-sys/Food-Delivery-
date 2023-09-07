package com.krishna.foodwave.User.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.krishna.foodwave.User.mvvm.db.ProductRepository

class ViewModelFactory(private  val productRepository: ProductRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductViewModel(productRepository) as T
    }

}