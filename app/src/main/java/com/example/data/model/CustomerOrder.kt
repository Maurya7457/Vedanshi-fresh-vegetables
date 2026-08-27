package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Locale

data class CartItem(
    val vegetable: Vegetable,
    val weightKg: Double, // e.g. 0.25, 0.5, 1.0, 1.5, 2.0
    val customUnitName: String = "" // "250g", "500g", "1 kg", etc.
) {
    val totalPrice: Double
        get() = vegetable.pricePerKg * weightKg

    val weightDisplay: String
        get() {
            if (customUnitName.isNotEmpty()) return customUnitName
            return when {
                weightKg == 0.25 -> "250 g"
                weightKg == 0.50 -> "500 g"
                weightKg == 0.75 -> "750 g"
                weightKg == 1.00 -> "1 kg"
                weightKg % 1.0 == 0.0 -> "${weightKg.toInt()} kg"
                else -> String.format(Locale.US, "%.2f kg", weightKg)
            }
        }

    val formattedPrice: String
        get() = if (totalPrice % 1.0 == 0.0) "₹${totalPrice.toInt()}" else String.format(Locale.US, "₹%.1f", totalPrice)
}

@Entity(tableName = "customer_orders")
data class CustomerOrder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val customerName: String,
    val customerPhone: String,
    val customerAddress: String,
    val deliveryNotes: String = "",
    val itemsSummary: String, // Clean textual summary or JSON of items
    val totalAmount: Double,
    val totalItemCount: Int,
    val status: String = "Pending", // Pending, Confirmed, Out for Delivery, Delivered, Cancelled
    val timestamp: Long = System.currentTimeMillis(),
    val paymentMode: String = "Cash on Delivery (COD)"
) {
    val formattedTotal: String
        get() = if (totalAmount % 1.0 == 0.0) "₹${totalAmount.toInt()}" else String.format(Locale.US, "₹%.1f", totalAmount)
}
