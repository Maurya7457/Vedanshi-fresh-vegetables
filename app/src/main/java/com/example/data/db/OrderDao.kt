package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CustomerOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM customer_orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<CustomerOrder>>

    @Query("SELECT * FROM customer_orders WHERE status = :status ORDER BY timestamp DESC")
    fun getOrdersByStatus(status: String): Flow<List<CustomerOrder>>

    @Query("SELECT * FROM customer_orders WHERE id = :id LIMIT 1")
    suspend fun getOrderById(id: Long): CustomerOrder?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: CustomerOrder): Long

    @Update
    suspend fun updateOrder(order: CustomerOrder)

    @Query("UPDATE customer_orders SET status = :status WHERE id = :id")
    suspend fun updateOrderStatus(id: Long, status: String)

    @Delete
    suspend fun deleteOrder(order: CustomerOrder)

    @Query("SELECT COUNT(*) FROM customer_orders")
    suspend fun getOrderCount(): Int

    @Query("SELECT SUM(totalAmount) FROM customer_orders WHERE status != 'Cancelled'")
    fun getTotalRevenue(): Flow<Double?>
}
