package com.krishna.foodwave.User.mvvm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.krishna.foodwave.Admin.model.AddProductModel

import com.krishna.foodwave.User.mvvm.model.ProductModel


@Database(entities = [ProductModel::class], version = 2)
abstract class ProductDataBase : RoomDatabase() {
    abstract fun prodctDao(): ProductDao
    companion object{
        private  var INSTANCE:ProductDataBase? = null
        fun getDatabase(context: Context) : ProductDataBase {
            if (INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,ProductDataBase::class.java
                    ,"ProductDataBase")
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()

                        .build()
                }

            }
            return  INSTANCE!!
        }
    }
}