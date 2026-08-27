package com.example.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Spa
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

object VegetableUiHelper {

    val CATEGORIES = listOf(
        "All",
        "Daily Essentials",
        "Green Leafy",
        "Root Veggies",
        "Gourds & Peas",
        "Seasonal Special"
    )

    fun getVegetableEmoji(iconKey: String): String {
        return when (iconKey.lowercase()) {
            "tomato" -> "🍅"
            "potato" -> "🥔"
            "onion" -> "🧅"
            "peas" -> "🫛"
            "cauliflower" -> "🥦"
            "spinach" -> "🥬"
            "bottle_gourd" -> "🥒"
            "carrot" -> "🥕"
            "ginger" -> "🫚"
            "chilli" -> "🌶️"
            "coriander" -> "🌿"
            "bhindi" -> "🌱"
            "brinjal" -> "🍆"
            "lemon" -> "🍋"
            "garlic" -> "🧄"
            "corn" -> "🌽"
            "capsicum" -> "🫑"
            else -> "🥬"
        }
    }

    fun getCategoryColor(category: String): Color {
        return when (category) {
            "Daily Essentials" -> Color(0xFFE65100)
            "Green Leafy" -> Color(0xFF2E7D32)
            "Root Veggies" -> Color(0xFF8D6E63)
            "Gourds & Peas" -> Color(0xFF00796B)
            "Seasonal Special" -> Color(0xFFC2185B)
            else -> Color(0xFF388E3C)
        }
    }

    fun getCategoryIcon(category: String): ImageVector {
        return when (category) {
            "Green Leafy" -> Icons.Default.Grass
            "Root Veggies" -> Icons.Default.Spa
            "Seasonal Special" -> Icons.Default.LocalFlorist
            else -> Icons.Default.Eco
        }
    }

    val STANDARD_WEIGHT_OPTIONS = listOf(
        WeightOption("250 g", 0.25),
        WeightOption("500 g", 0.50),
        WeightOption("1 kg", 1.00),
        WeightOption("2 kg", 2.00)
    )
}

data class WeightOption(
    val label: String,
    val weightKg: Double
)
