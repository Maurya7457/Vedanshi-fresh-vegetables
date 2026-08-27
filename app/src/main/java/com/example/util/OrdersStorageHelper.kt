package com.example.util

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.CartItem
import com.example.data.model.CustomerOrder
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * LocalStorage / SharedPreferences manager for "vedanshi_orders".
 * Provides redundant local persistence matching the exact JSON structure:
 * { id, name, mobile, address, items [{sabziName, qty, price}], totalAmount, date, status: "New" }
 */
object OrdersStorageHelper {
    private const val PREFS_NAME = "vedanshi_fresh_prefs"
    private const val KEY_ORDERS = "vedanshi_orders"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Save an order to SharedPreferences under "vedanshi_orders"
     */
    fun saveOrderToLocalStorage(
        context: Context,
        orderId: Long,
        name: String,
        mobile: String,
        address: String,
        cartItems: List<CartItem>,
        totalAmount: Double,
        status: String = "New"
    ) {
        try {
            val prefs = getPrefs(context)
            val existingJson = prefs.getString(KEY_ORDERS, "[]") ?: "[]"
            val ordersArray = JSONArray(existingJson)

            val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            val dateStr = dateFormat.format(Date())

            val itemsArray = JSONArray()
            for (item in cartItems) {
                val itemObj = JSONObject().apply {
                    put("sabziName", "${item.vegetable.name} (${item.vegetable.hindiName})")
                    put("qty", item.weightDisplay)
                    put("price", item.totalPrice)
                }
                itemsArray.put(itemObj)
            }

            val newOrderObj = JSONObject().apply {
                put("id", orderId)
                put("name", name.ifBlank { "Valued Customer" })
                put("mobile", mobile)
                put("address", address.ifBlank { "Local Nawabganj / Bijauriya" })
                put("items", itemsArray)
                put("totalAmount", totalAmount)
                put("date", dateStr)
                put("status", status)
                put("timestamp", System.currentTimeMillis())
            }

            // Put new order at the front (newest first)
            val newOrdersArray = JSONArray()
            newOrdersArray.put(newOrderObj)
            for (i in 0 until ordersArray.length()) {
                newOrdersArray.put(ordersArray.getJSONObject(i))
            }

            prefs.edit().putString(KEY_ORDERS, newOrdersArray.toString()).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Retrieve all orders raw JSON string
     */
    fun getRawOrdersJson(context: Context): String {
        return getPrefs(context).getString(KEY_ORDERS, "[]") ?: "[]"
    }

    /**
     * Clear all saved orders in localStorage
     */
    fun clearAllOrders(context: Context) {
        getPrefs(context).edit().remove(KEY_ORDERS).apply()
    }
}
