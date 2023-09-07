package com.krishna.foodwave.Admin.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class AddProductModel(

    val productId:String="",
//    @ColumnInfo(name = "ProductName")
    val productName:String?="",
    val productDescription:String?="",
    val productCoverImg:String?="",
    val productCategory:String?="",
//    @ColumnInfo(name = "ProductMrp")
    val productMrp:String?="",
    val productSp:String?="",
//    @ColumnInfo(name = "ProductImage")
    val productImages: ArrayList<String> = ArrayList()

)
