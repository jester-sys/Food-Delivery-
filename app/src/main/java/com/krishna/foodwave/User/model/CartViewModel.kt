package com.krishna.foodwave.User.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {
    // LiveData to hold the total price for each product based on product ID
    val productTotalPriceLiveData: MutableLiveData<Map<String, Int>> = MutableLiveData()

    // Function to update the total price for a specific product
    fun updateTotalPriceForProduct(productId: String, totalPrice: Int) {
        val currentMap = productTotalPriceLiveData.value ?: mapOf()
        val updatedMap = currentMap.toMutableMap().apply { this[productId] = totalPrice }
        productTotalPriceLiveData.value = updatedMap
    }}