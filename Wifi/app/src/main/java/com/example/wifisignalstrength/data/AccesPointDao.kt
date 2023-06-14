package com.example.wifisignalstrength.data

import androidx.room.*
import com.example.wifisignalstrength.model.AccesPoint

@Dao
interface AccesPointDao {

    @Query("SELECT * FROM accessPoint_database")
    suspend fun getInfo(): List<AccesPoint>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(accespoint: AccesPoint)

    @Delete
    suspend fun delete(accespoint: AccesPoint)

}