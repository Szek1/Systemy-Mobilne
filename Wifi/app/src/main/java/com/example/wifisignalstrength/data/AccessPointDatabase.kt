package com.example.wifisignalstrength.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wifisignalstrength.model.AccesPoint

@Database(entities = [AccesPoint::class], version = 1, exportSchema = false)
abstract class AccessPointDatabase: RoomDatabase() {

    abstract fun accespointDao(): AccesPointDao

//    companion object{
//        @Volatile
//        private var INSTANCE: AccessPointDatabase? = null
//
//        fun getDatabase(context: Context): AccessPointDatabase{
//            return INSTANCE?: synchronized(this){
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AccessPointDatabase::class.java,
//                    "accessPoint_database"
//                )
//                    .fallbackToDestructiveMigration()
//                    .build()
//                INSTANCE = instance
//                return instance
//            }
//        }
////
//    }
}