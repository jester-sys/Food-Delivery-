package com.krishna.foodwave.User.mvvm.model

import android.annotation.SuppressLint
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ProductData")
data class ProductModel(
//
   @PrimaryKey
    @NonNull
    val productId:String="",
//    @ColumnInfo(name = "ProductName")
    val productName:String?="",
    //    @ColumnInfo(name = "ProductMrp")
    val productMrp:String?="",
    //    @ColumnInfo(name = "ProductImage")
    val productImages: String?,
    val productSp:String?=""

)
