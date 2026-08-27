package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Vegetable
import com.example.util.VegetableUiHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditVegetableDialog(
    vegetable: Vegetable?,
    onDismiss: () -> Unit,
    onSave: (Vegetable) -> Unit
) {
    val isEditMode = vegetable != null

    var name by remember { mutableStateOf(vegetable?.name ?: "") }
    var hindiName by remember { mutableStateOf(vegetable?.hindiName ?: "") }
    var category by remember { mutableStateOf(vegetable?.category ?: "Daily Essentials") }
    var pricePerKgText by remember { mutableStateOf(vegetable?.pricePerKg?.toString() ?: "") }
    var stockKgText by remember { mutableStateOf(vegetable?.stockKg?.toString() ?: "50") }
    var isInStock by remember { mutableStateOf(vegetable?.isInStock ?: true) }
    var description by remember { mutableStateOf(vegetable?.description ?: "") }
    var selectedIconKey by remember { mutableStateOf(vegetable?.iconKey ?: "tomato") }

    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }

    val iconOptions = listOf(
        "tomato", "potato", "onion", "peas", "cauliflower", "spinach",
        "bottle_gourd", "carrot", "ginger", "chilli", "coriander", "bhindi",
        "brinjal", "lemon", "garlic", "corn", "capsicum"
    )

    val categories = VegetableUiHelper.CATEGORIES.filter { it != "All" }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 16.dp)
                .testTag("add_edit_vegetable_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Dialog Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isEditMode) "Edit Vegetable" else "Add New Vegetable",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Icon / Vegetable Emoji Selector
                Text(
                    text = "Select Vegetable Icon / Visual:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(iconOptions) { iconKey ->
                        val isSelected = selectedIconKey == iconKey
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                            modifier = Modifier
                                .size(48.dp)
                                .clickable { selectedIconKey = iconKey }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = VegetableUiHelper.getVegetableEmoji(iconKey),
                                    fontSize = 24.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // English Name
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        if (it.isNotBlank()) nameError = false
                    },
                    label = { Text("Vegetable Name (English) *") },
                    placeholder = { Text("e.g. Fresh Red Tomatoes") },
                    isError = nameError,
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("veg_name_input")
                )
                if (nameError) {
                    Text(
                        text = "Name is required",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Hindi Name
                OutlinedTextField(
                    value = hindiName,
                    onValueChange = { hindiName = it },
                    label = { Text("Hindi Name (Optional)") },
                    placeholder = { Text("e.g. देसी लाल टमाटर") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("veg_hindi_name_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoryMenuExpanded,
                    onExpandedChange = { categoryMenuExpanded = !categoryMenuExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryMenuExpanded) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = categoryMenuExpanded,
                        onDismissRequest = { categoryMenuExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    categoryMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Price Per Kg
                OutlinedTextField(
                    value = pricePerKgText,
                    onValueChange = {
                        pricePerKgText = it
                        if (it.isNotBlank() && it.toDoubleOrNull() != null) priceError = false
                    },
                    label = { Text("Price Per 1 Kg (₹) *") },
                    placeholder = { Text("e.g. 40") },
                    isError = priceError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("veg_price_input")
                )
                if (priceError) {
                    Text(
                        text = "Please enter a valid price (e.g. 40)",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                // Quick price preview for 250g / 500g
                val priceVal = pricePerKgText.toDoubleOrNull() ?: 0.0
                if (priceVal > 0.0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Auto Calculated: 250g = ₹${(priceVal * 0.25).toInt()} • 500g = ₹${(priceVal * 0.50).toInt()} • 1kg = ₹${priceVal.toInt()}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Stock In Kg
                OutlinedTextField(
                    value = stockKgText,
                    onValueChange = { stockKgText = it },
                    label = { Text("Stock Available (in Kg)") },
                    placeholder = { Text("e.g. 50") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Freshness Description / Notes") },
                    placeholder = { Text("e.g. Freshly harvested from local Nawabganj farm.") },
                    minLines = 2,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // In Stock Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Available In Stock",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (isInStock) "Customers can order this item" else "Marked as Out of Stock",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Switch(
                        checked = isInStock,
                        onCheckedChange = { isInStock = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Save & Cancel Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            var valid = true
                            if (name.isBlank()) {
                                nameError = true
                                valid = false
                            }
                            val price = pricePerKgText.toDoubleOrNull()
                            if (price == null || price <= 0.0) {
                                priceError = true
                                valid = false
                            }

                            if (valid) {
                                val saved = (vegetable ?: Vegetable(
                                    name = name.trim(),
                                    pricePerKg = price ?: 0.0
                                )).copy(
                                    name = name.trim(),
                                    hindiName = hindiName.trim(),
                                    category = category,
                                    pricePerKg = price ?: 0.0,
                                    stockKg = stockKgText.toDoubleOrNull() ?: 50.0,
                                    isInStock = isInStock,
                                    description = description.trim(),
                                    iconKey = selectedIconKey
                                )
                                onSave(saved)
                            }
                        },
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp)
                            .testTag("save_vegetable_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = if (isEditMode) "Save Changes" else "Add Vegetable",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
