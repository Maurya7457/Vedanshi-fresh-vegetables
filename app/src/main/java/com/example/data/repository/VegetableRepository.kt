package com.example.data.repository

import com.example.data.db.VegetableDao
import com.example.data.model.Vegetable
import kotlinx.coroutines.flow.Flow

class VegetableRepository(private val vegetableDao: VegetableDao) {

    val allVegetables: Flow<List<Vegetable>> = vegetableDao.getAllVegetables()

    fun getVegetablesByCategory(category: String): Flow<List<Vegetable>> {
        return if (category == "All") {
            vegetableDao.getAllVegetables()
        } else {
            vegetableDao.getVegetablesByCategory(category)
        }
    }

    suspend fun getVegetableById(id: Long): Vegetable? = vegetableDao.getVegetableById(id)

    suspend fun insertVegetable(vegetable: Vegetable): Long = vegetableDao.insertVegetable(vegetable)

    suspend fun updateVegetable(vegetable: Vegetable) = vegetableDao.updateVegetable(vegetable)

    suspend fun updateStockStatus(id: Long, inStock: Boolean) = vegetableDao.updateStockStatus(id, inStock)

    suspend fun updatePrice(id: Long, price: Double) = vegetableDao.updatePrice(id, price)

    suspend fun deleteVegetable(vegetable: Vegetable) = vegetableDao.deleteVegetable(vegetable)

    suspend fun checkAndSeedInitialData() {
        if (vegetableDao.getVegetableCount() == 0) {
            vegetableDao.insertAll(com.example.data.sample.InitialVegetableData.getInitialVegetables())
        }
    }
}
