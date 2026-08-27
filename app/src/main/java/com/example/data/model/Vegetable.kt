package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Locale

@Entity(tableName = "vegetables")
data class Vegetable(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val hindiName: String = "",
    val category: String = "Daily Essentials",
    val pricePerKg: Double,
    val unit: String = "kg",
    val stockKg: Double = 50.0,
    val isInStock: Boolean = true,
    val description: String = "",
    val isFeatured: Boolean = false,
    val iconKey: String = "tomato" // tomato, potato, onion, peas, cauliflower, spinach, bottle_gourd, carrot, ginger, chilli, coriander, bhindi, brinjal, lemon, generic
) {
    fun getPriceForWeight(weightKg: Double): Double {
        return pricePerKg * weightKg
    }

    fun getFormattedPriceForWeight(weightKg: Double): String {
        val price = getPriceForWeight(weightKg)
        return if (price % 1.0 == 0.0) {
            "₹${price.toInt()}"
        } else {
            String.format(Locale.US, "₹%.1f", price)
        }
    }

    val price250g: String
        get() = getFormattedPriceForWeight(0.25)

    val price500g: String
        get() = getFormattedPriceForWeight(0.50)

    val price1kg: String
        get() = getFormattedPriceForWeight(1.00)
}
