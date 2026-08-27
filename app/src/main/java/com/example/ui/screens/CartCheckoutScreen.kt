package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditLocation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CartItem
import com.example.ui.viewmodel.CustomerTab
import com.example.ui.viewmodel.VegetableViewModel
import com.example.util.OrderCommunicationHelper
import com.example.util.VegetableUiHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartCheckoutScreen(
    viewModel: VegetableViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val cartTotalAmount by viewModel.cartTotalAmount.collectAsStateWithLifecycle()
    val checkoutInfo by viewModel.checkoutInfo.collectAsStateWithLifecycle()

    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var addressError by remember { mutableStateOf(false) }

    val quickAddressPresets = listOf(
        "Nawabganj Main Market",
        "Bijauriya Village",
        "Nawabganj Town",
        "Railway Station Road, Nawabganj"
    )

    if (cartItems.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(96.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🛒", fontSize = 48.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Your Cart is Empty",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Add fresh vegetables directly from our farm catalog.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { viewModel.setCustomerTab(CustomerTab.SHOP) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("browse_veggies_empty_cart_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "Browse Vegetables"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Explore Fresh Vegetables")
                }
            }
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("cart_checkout_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.setCustomerTab(CustomerTab.SHOP) },
                        modifier = Modifier.testTag("cart_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Shop"
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "My Fresh Cart (${cartItems.size})",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                FilledTonalButton(
                    onClick = { viewModel.clearCart() },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("Clear Cart", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        // Cart Items Section
        item {
            Text(
                text = "Selected Vegetables",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        items(cartItems, key = { it.vegetable.id }) { item ->
            CartItemRow(
                cartItem = item,
                onWeightChanged = { newWeight ->
                    viewModel.setCartItemWeight(item.vegetable.id, newWeight)
                },
                onRemove = {
                    viewModel.removeFromCart(item.vegetable.id)
                }
            )
        }

        // Bill Summary Card
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Bill Details",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Item Total (${cartItems.size} items)",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Text(
                            text = "₹${if (cartTotalAmount % 1.0 == 0.0) cartTotalAmount.toInt().toString() else String.format("%.1f", cartTotalAmount)}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Local Delivery (Nawabganj & Bijauriya)",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Text(
                            text = "FREE",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Grand Total Amount",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(
                            text = "₹${if (cartTotalAmount % 1.0 == 0.0) cartTotalAmount.toInt().toString() else String.format("%.1f", cartTotalAmount)}",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        }

        // Customer Details & Delivery Address Form
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Delivery",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Customer & Delivery Details",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Customer Name Field
                    OutlinedTextField(
                        value = checkoutInfo.name,
                        onValueChange = {
                            viewModel.updateCheckoutInfo(name = it)
                            if (it.isNotBlank()) nameError = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_name_input"),
                        label = { Text("Customer Name *") },
                        placeholder = { Text("e.g. Vishal Maurya") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "Name")
                        },
                        isError = nameError,
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                    if (nameError) {
                        Text(
                            text = "Please enter your name",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Phone Number Field
                    OutlinedTextField(
                        value = checkoutInfo.phone,
                        onValueChange = {
                            viewModel.updateCheckoutInfo(phone = it)
                            if (it.length >= 10) phoneError = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_phone_input"),
                        label = { Text("Phone Number *") },
                        placeholder = { Text("10-digit mobile number") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = "Phone")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        isError = phoneError,
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                    if (phoneError) {
                        Text(
                            text = "Please enter a valid 10-digit phone number",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Delivery Address Field
                    OutlinedTextField(
                        value = checkoutInfo.address,
                        onValueChange = {
                            viewModel.updateCheckoutInfo(address = it)
                            if (it.isNotBlank()) addressError = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_address_input"),
                        label = { Text("Delivery Address (House/Shop, Locality, Area) *") },
                        placeholder = { Text("e.g. Near Shiv Mandir, Bijauriya, Nawabganj") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.EditLocation, contentDescription = "Address")
                        },
                        isError = addressError,
                        minLines = 2,
                        shape = RoundedCornerShape(10.dp)
                    )
                    if (addressError) {
                        Text(
                            text = "Please enter delivery address in Nawabganj/Bijauriya",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quick address tags
                    Text(
                        text = "Quick Select Local Area:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        quickAddressPresets.take(2).forEach { area ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                modifier = Modifier.clickable {
                                    val current = checkoutInfo.address
                                    val updated = if (current.isBlank()) area else "$current, $area"
                                    viewModel.updateCheckoutInfo(address = updated)
                                    addressError = false
                                }
                            ) {
                                Text(
                                    text = "+ $area",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Delivery Notes Field
                    OutlinedTextField(
                        value = checkoutInfo.deliveryNotes,
                        onValueChange = { viewModel.updateCheckoutInfo(notes = it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_notes_input"),
                        label = { Text("Delivery Note / Special Instructions (Optional)") },
                        placeholder = { Text("e.g. Call before reaching, fresh leafy items only") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Notes, contentDescription = "Notes")
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Payment Method Radio Options
                    Text(
                        text = "Payment Mode:",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    listOf(
                        "Cash on Delivery (COD)",
                        "UPI on Delivery (GPay / PhonePe / Paytm)"
                    ).forEach { method ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.updateCheckoutInfo(paymentMode = method) }
                                .padding(vertical = 2.dp)
                        ) {
                            RadioButton(
                                selected = checkoutInfo.paymentMode == method,
                                onClick = { viewModel.updateCheckoutInfo(paymentMode = method) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.primary
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = method,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (checkoutInfo.paymentMode == method) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        }
                    }
                }
            }
        }

        // Action Order Buttons
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Primary: Place Order Button
                Button(
                    onClick = {
                        val isValid = validateCheckoutForm(
                            checkoutInfo.name,
                            checkoutInfo.phone,
                            checkoutInfo.address,
                            onNameError = { nameError = it },
                            onPhoneError = { phoneError = it },
                            onAddressError = { addressError = it }
                        )

                        if (isValid) {
                            viewModel.placeOrder { savedOrder ->
                                Toast.makeText(context, "Order Placed Successfully!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Please fill required details", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("place_order_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Place Order")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Place Order & Pay on Delivery",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                // WhatsApp Instant Ordering Button
                Button(
                    onClick = {
                        OrderCommunicationHelper.sendCartOrderToWhatsApp(
                            context = context,
                            customerName = checkoutInfo.name.trim(),
                            customerPhone = checkoutInfo.phone.trim(),
                            customerAddress = checkoutInfo.address.trim(),
                            deliveryNotes = checkoutInfo.deliveryNotes.trim(),
                            cartItems = cartItems,
                            totalAmount = cartTotalAmount,
                            paymentMode = checkoutInfo.paymentMode
                        )
                        // Also record order in app
                        viewModel.placeOrder {}
                        Toast.makeText(context, "Opening WhatsApp with your vegetable order...", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("whatsapp_order_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF25D366),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Chat,
                        contentDescription = "Order on WhatsApp",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Order via WhatsApp (${OrderCommunicationHelper.SELLER_PHONE})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                // Call to Order Button
                OutlinedButton(
                    onClick = { OrderCommunicationHelper.callSeller(context) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("call_to_order_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = "Call Seller")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Direct Call Order: ${OrderCommunicationHelper.SELLER_PHONE}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

private fun validateCheckoutForm(
    name: String,
    phone: String,
    address: String,
    onNameError: (Boolean) -> Unit,
    onPhoneError: (Boolean) -> Unit,
    onAddressError: (Boolean) -> Unit
): Boolean {
    var valid = true
    if (name.isBlank()) {
        onNameError(true)
        valid = false
    } else {
        onNameError(false)
    }

    if (phone.isBlank() || phone.length < 10) {
        onPhoneError(true)
        valid = false
    } else {
        onPhoneError(false)
    }

    if (address.isBlank()) {
        onAddressError(true)
        valid = false
    } else {
        onAddressError(false)
    }
    return valid
}

@Composable
private fun CartItemRow(
    cartItem: CartItem,
    onWeightChanged: (Double) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("cart_item_${cartItem.vegetable.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = VegetableUiHelper.getVegetableEmoji(cartItem.vegetable.iconKey),
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cartItem.vegetable.name,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "${cartItem.weightDisplay} • @ ₹${cartItem.vegetable.pricePerKg.toInt()}/kg",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = cartItem.formattedPrice,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }

            // Weight Modifier Stepper
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                IconButton(
                    onClick = {
                        val newWeight = cartItem.weightKg - 0.25
                        if (newWeight <= 0) onRemove() else onWeightChanged(newWeight)
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (cartItem.weightKg <= 0.25) Icons.Default.Delete else Icons.Default.Remove,
                        contentDescription = "Decrease",
                        modifier = Modifier.size(16.dp),
                        tint = if (cartItem.weightKg <= 0.25) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = cartItem.weightDisplay,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 6.dp)
                )

                IconButton(
                    onClick = {
                        onWeightChanged(cartItem.weightKg + 0.25)
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
