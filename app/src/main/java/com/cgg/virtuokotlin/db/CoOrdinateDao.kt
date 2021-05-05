package com.cgg.virtuokotlin.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cgg.virtuokotlin.source.CoOrdinates

@Dao
interface CoOrdinateDao {
    @Query("SELECT * FROM Location_Info")
    suspend fun getCoOrdinates(): List<CoOrdinates>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoOrdinates(coOrdinates: List<CoOrdinates>)
}