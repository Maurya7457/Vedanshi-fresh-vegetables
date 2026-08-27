package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Vegetable
import kotlinx.coroutines.flow.Flow

@Dao
interface VegetableDao {
    @Query("SELECT * FROM vegetables ORDER BY isInStock DESC, name ASC")
    fun getAllVegetables(): Flow<List<Vegetable>>

    @Query("SELECT * FROM vegetables WHERE category = :category ORDER BY isInStock DESC, name ASC")
    fun getVegetablesByCategory(category: String): Flow<List<Vegetable>>

    @Query("SELECT * FROM vegetables WHERE id = :id LIMIT 1")
    suspend fun getVegetableById(id: Long): Vegetable?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVegetable(vegetable: Vegetable): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vegetables: List<Vegetable>)

    @Update
    suspend fun updateVegetable(vegetable: Vegetable)

    @Delete
    suspend fun deleteVegetable(vegetable: Vegetable)

    @Query("UPDATE vegetables SET isInStock = :inStock WHERE id = :id")
    suspend fun updateStockStatus(id: Long, inStock: Boolean)

    @Query("UPDATE vegetables SET pricePerKg = :price WHERE id = :id")
    suspend fun updatePrice(id: Long, price: Double)

    @Query("SELECT COUNT(*) FROM vegetables")
    suspend fun getVegetableCount(): Int
}
