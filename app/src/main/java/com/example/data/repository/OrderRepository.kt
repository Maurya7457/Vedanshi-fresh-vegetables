package com.example.data.repository

import com.example.data.db.OrderDao
import com.example.data.model.CustomerOrder
import kotlinx.coroutines.flow.Flow

class OrderRepository(private val orderDao: OrderDao) {

    val allOrders: Flow<List<CustomerOrder>> = orderDao.getAllOrders()

    fun getOrdersByStatus(status: String): Flow<List<CustomerOrder>> {
        return if (status == "All") {
            orderDao.getAllOrders()
        } else {
            orderDao.getOrdersByStatus(status)
        }
    }

    val totalRevenue: Flow<Double?> = orderDao.getTotalRevenue()

    suspend fun insertOrder(order: CustomerOrder): Long = orderDao.insertOrder(order)

    suspend fun updateOrderStatus(id: Long, status: String) = orderDao.updateOrderStatus(id, status)

    suspend fun deleteOrder(order: CustomerOrder) = orderDao.deleteOrder(order)
}
