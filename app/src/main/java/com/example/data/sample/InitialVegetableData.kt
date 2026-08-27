package com.example.data.sample

import com.example.data.model.Vegetable

object InitialVegetableData {
    fun getInitialVegetables(): List<Vegetable> = listOf(
        Vegetable(
            name = "Fresh Red Tomatoes",
            hindiName = "देसी लाल टमाटर",
            category = "Daily Essentials",
            pricePerKg = 40.0,
            unit = "kg",
            stockKg = 50.0,
            isInStock = true,
            description = "Farm-fresh, ripe red juicy tomatoes harvested locally. Perfect for curries, salads and daily cooking.",
            isFeatured = true,
            iconKey = "tomato"
        ),
        Vegetable(
            name = "Farm Fresh Potatoes",
            hindiName = "नया पहाड़ी आलू",
            category = "Root Veggies",
            pricePerKg = 30.0,
            unit = "kg",
            stockKg = 80.0,
            isInStock = true,
            description = "Clean, firm, dirt-free potatoes directly sourced from farmers. Staple for all Indian dishes.",
            isFeatured = true,
            iconKey = "potato"
        ),
        Vegetable(
            name = "Fresh Red Onions",
            hindiName = "ताजा नासिक प्याज",
            category = "Daily Essentials",
            pricePerKg = 35.0,
            unit = "kg",
            stockKg = 70.0,
            isInStock = true,
            description = "Pungent and crisp quality red onions. Hand-graded for optimal freshness and long shelf life.",
            isFeatured = true,
            iconKey = "onion"
        ),
        Vegetable(
            name = "Green Peas / Matar",
            hindiName = "ताजा हरी मटर",
            category = "Seasonal Special",
            pricePerKg = 60.0,
            unit = "kg",
            stockKg = 35.0,
            isInStock = true,
            description = "Sweet, tender and freshly plucked green pea pods. Full of natural sweetness.",
            isFeatured = true,
            iconKey = "peas"
        ),
        Vegetable(
            name = "Fresh Cauliflower",
            hindiName = "सफेद फूल गोभी",
            category = "Daily Essentials",
            pricePerKg = 40.0,
            unit = "kg",
            stockKg = 40.0,
            isInStock = true,
            description = "Bright white, tight curd cauliflower heads packed with vitamins and crunch.",
            isFeatured = false,
            iconKey = "cauliflower"
        ),
        Vegetable(
            name = "Tender Spinach / Palak",
            hindiName = "ताजा देशी पालक",
            category = "Green Leafy",
            pricePerKg = 30.0,
            unit = "kg",
            stockKg = 25.0,
            isInStock = true,
            description = "Lush green, iron-rich spinach leaves freshly washed and bundled for your health.",
            isFeatured = true,
            iconKey = "spinach"
        ),
        Vegetable(
            name = "Green Bottle Gourd / Lauki",
            hindiName = "नरम हरी लौकी",
            category = "Gourds & Peas",
            pricePerKg = 30.0,
            unit = "kg",
            stockKg = 30.0,
            isInStock = true,
            description = "Tender, juicy, pesticide-free fresh bottle gourd ideal for nutritious soups and light sabji.",
            isFeatured = false,
            iconKey = "bottle_gourd"
        ),
        Vegetable(
            name = "Crunchy Red Carrots / Gajar",
            hindiName = "देसी लाल गाजर",
            category = "Root Veggies",
            pricePerKg = 45.0,
            unit = "kg",
            stockKg = 30.0,
            isInStock = true,
            description = "Sweet and crisp red carrots, great for salads, healthy juices and homemade halwa.",
            isFeatured = false,
            iconKey = "carrot"
        ),
        Vegetable(
            name = "Fresh Ginger / Adrak",
            hindiName = "ताजा अदरक",
            category = "Daily Essentials",
            pricePerKg = 120.0,
            unit = "kg",
            stockKg = 15.0,
            isInStock = true,
            description = "Aromatic, fiber-rich fresh ginger with strong aroma for immunity and chai.",
            isFeatured = false,
            iconKey = "ginger"
        ),
        Vegetable(
            name = "Spicy Green Chillies",
            hindiName = "तीखी हरी मिर्च",
            category = "Daily Essentials",
            pricePerKg = 80.0,
            unit = "kg",
            stockKg = 15.0,
            isInStock = true,
            description = "Crisp, pungent fresh green chillies picked at prime heat and freshness.",
            isFeatured = false,
            iconKey = "chilli"
        ),
        Vegetable(
            name = "Fresh Coriander / Dhaniya",
            hindiName = "खुशबूदार हरा धनिया",
            category = "Green Leafy",
            pricePerKg = 60.0,
            unit = "kg",
            stockKg = 10.0,
            isInStock = true,
            description = "Super fragrant coriander leaves to garnish every meal with fresh aroma.",
            isFeatured = false,
            iconKey = "coriander"
        ),
        Vegetable(
            name = "Crisp Ladyfinger / Bhindi",
            hindiName = "ताजा हरी भिंडी",
            category = "Gourds & Peas",
            pricePerKg = 50.0,
            unit = "kg",
            stockKg = 25.0,
            isInStock = true,
            description = "Small, tender non-fibrous ladyfinger pods, snap-tested for peak freshness.",
            isFeatured = true,
            iconKey = "bhindi"
        ),
        Vegetable(
            name = "Purple Brinjal / Baingan",
            hindiName = "गोल काला बैंगन (भर्ता)",
            category = "Daily Essentials",
            pricePerKg = 35.0,
            unit = "kg",
            stockKg = 20.0,
            isInStock = true,
            description = "Glossy, seedless large brinjals ideal for authentic Baingan Bharta.",
            isFeatured = false,
            iconKey = "brinjal"
        ),
        Vegetable(
            name = "Juicy Fresh Lemons / Nimbu",
            hindiName = "रसदार पीला नींबू",
            category = "Daily Essentials",
            pricePerKg = 80.0,
            unit = "kg",
            stockKg = 15.0,
            isInStock = true,
            description = "Thin-skinned lemons bursting with vitamin C and refreshing citrus juice.",
            isFeatured = false,
            iconKey = "lemon"
        )
    )
}
