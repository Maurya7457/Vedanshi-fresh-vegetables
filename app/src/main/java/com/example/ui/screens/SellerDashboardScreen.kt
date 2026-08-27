package com.example.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CustomerOrder
import com.example.data.model.Vegetable
import com.example.ui.components.StoreHeader
import com.example.ui.viewmodel.AppMode
import com.example.ui.viewmodel.SellerTab
import com.example.ui.viewmodel.VegetableViewModel
import com.example.util.OrderCommunicationHelper
import com.example.util.VegetableUiHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerDashboardScreen(
    viewModel: VegetableViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentMode by viewModel.currentMode.collectAsStateWithLifecycle()
    val sellerTab by viewModel.sellerTab.collectAsStateWithLifecycle()
    val allVegetables by viewModel.allVegetables.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val filteredSellerOrders by viewModel.filteredSellerOrders.collectAsStateWithLifecycle()
    val isAddEditDialogVisible by viewModel.isAddEditDialogVisible.collectAsStateWithLifecycle()
    val editingVegetable by viewModel.editingVegetable.collectAsStateWithLifecycle()

    val pendingOrdersCount = allOrders.count { it.status == "Pending" || it.status == "Confirmed" || it.status == "Out for Delivery" }
    val totalRevenue = allOrders.filter { it.status != "Cancelled" }.sumOf { it.totalAmount }

    if (isAddEditDialogVisible) {
        AddEditVegetableDialog(
            vegetable = editingVegetable,
            onDismiss = { viewModel.closeAddEditDialog() },
            onSave = { viewModel.saveVegetable(it) }
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Store Header
            StoreHeader(
                currentMode = currentMode,
                onModeChange = { viewModel.setMode(it) }
            )

            // Seller Tabs
            PrimaryTabRow(
                selectedTabIndex = sellerTab.ordinal,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = sellerTab == SellerTab.ORDERS,
                    onClick = { viewModel.setSellerTab(SellerTab.ORDERS) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.ListAlt, contentDescription = "Admin Orders", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Admin Panel - All Orders (${allOrders.size})", fontWeight = FontWeight.Bold)
                        }
                    },
                    modifier = Modifier.testTag("seller_orders_tab")
                )

                Tab(
                    selected = sellerTab == SellerTab.INVENTORY,
                    onClick = { viewModel.setSellerTab(SellerTab.INVENTORY) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Inventory2, contentDescription = "Inventory", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Veg Inventory (${allVegetables.size})")
                        }
                    },
                    modifier = Modifier.testTag("seller_inventory_tab")
                )

                Tab(
                    selected = sellerTab == SellerTab.STORE_INFO,
                    onClick = { viewModel.setSellerTab(SellerTab.STORE_INFO) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Store, contentDescription = "Store Profile", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Store Info")
                        }
                    },
                    modifier = Modifier.testTag("seller_store_tab")
                )
            }

            // Tab Content
            when (sellerTab) {
                SellerTab.INVENTORY -> {
                    SellerInventoryView(
                        vegetables = allVegetables,
                        onAddVegetable = { viewModel.openAddVegetableDialog() },
                        onEditVegetable = { viewModel.openEditVegetableDialog(it) },
                        onToggleStock = { viewModel.toggleStockStatus(it) },
                        onDeleteVegetable = { viewModel.deleteVegetable(it) }
                    )
                }
                SellerTab.ORDERS -> {
                    SellerOrdersView(
                        orders = filteredSellerOrders,
                        pendingCount = pendingOrdersCount,
                        totalRevenue = totalRevenue,
                        onUpdateStatus = { id, status -> viewModel.updateOrderStatus(id, status) },
                        onDeleteOrder = { viewModel.deleteOrder(it) }
                    )
                }
                SellerTab.STORE_INFO -> {
                    SellerStoreInfoView()
                }
            }
        }

        // Floating Action Button to Add Vegetable (Only in Inventory tab)
        if (sellerTab == SellerTab.INVENTORY) {
            FloatingActionButton(
                onClick = { viewModel.openAddVegetableDialog() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .testTag("seller_add_vegetable_fab")
            ) {
                Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Vegetable")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add Vegetable", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun SellerInventoryView(
    vegetables: List<Vegetable>,
    onAddVegetable: () -> Unit,
    onEditVegetable: (Vegetable) -> Unit,
    onToggleStock: (Vegetable) -> Unit,
    onDeleteVegetable: (Vegetable) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("seller_inventory_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Vegetables Catalog Management",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                Text(
                    text = "${vegetables.count { it.isInStock }} In-Stock",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        items(vegetables, key = { it.id }) { vegetable ->
            SellerVegetableItemCard(
                vegetable = vegetable,
                onEdit = { onEditVegetable(vegetable) },
                onToggleStock = { onToggleStock(vegetable) },
                onDelete = { onDeleteVegetable(vegetable) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}

@Composable
private fun SellerVegetableItemCard(
    vegetable: Vegetable,
    onEdit: () -> Unit,
    onToggleStock: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("seller_veg_card_${vegetable.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = VegetableUiHelper.getVegetableEmoji(vegetable.iconKey),
                        fontSize = 26.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = vegetable.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    if (vegetable.hindiName.isNotBlank()) {
                        Text(
                            text = vegetable.hindiName,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                    Text(
                        text = "${vegetable.category} • ₹${vegetable.pricePerKg.toInt()}/kg",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                // In stock toggle
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Switch(
                        checked = vegetable.isInStock,
                        onCheckedChange = { onToggleStock() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.size(36.dp)
                    )
                    Text(
                        text = if (vegetable.isInStock) "In Stock" else "Out",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (vegetable.isInStock) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))

            // Price tiers & actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "250g: ${vegetable.price250g} | 500g: ${vegetable.price500g} | 1kg: ${vegetable.price1kg}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                )

                Row {
                    IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SellerOrdersView(
    orders: List<CustomerOrder>,
    pendingCount: Int,
    totalRevenue: Double,
    onUpdateStatus: (Long, String) -> Unit,
    onDeleteOrder: (CustomerOrder) -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("seller_orders_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Summary Metrics
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ElevatedCard(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Active Orders",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onPrimaryContainer)
                        )
                        Text(
                            text = "$pendingCount",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }

                ElevatedCard(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Total Revenue",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSecondaryContainer)
                        )
                        Text(
                            text = "₹${if (totalRevenue % 1.0 == 0.0) totalRevenue.toInt().toString() else String.format("%.1f", totalRevenue)}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        )
                    }
                }
            }
        }

        if (orders.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📦", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No Customer Orders Yet",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Orders placed by customers will appear here for management.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        } else {
            items(orders, key = { it.id }) { order ->
                SellerOrderCard(
                    order = order,
                    onStatusChange = { newStatus -> onUpdateStatus(order.id, newStatus) },
                    onCallCustomer = { OrderCommunicationHelper.callCustomer(context, order.customerPhone) },
                    onWhatsAppCustomer = { OrderCommunicationHelper.sendOrderDetailsToWhatsApp(context, order, order.customerPhone) },
                    onDelete = { onDeleteOrder(order) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SellerOrderCard(
    order: CustomerOrder,
    onStatusChange: (String) -> Unit,
    onCallCustomer: () -> Unit,
    onWhatsAppCustomer: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    var menuExpanded by remember { mutableStateOf(false) }

    val statusOptions = listOf(
        "New",
        "Pending",
        "Confirmed",
        "Out for Delivery",
        "Delivered",
        "Cancelled"
    )

    val statusColor = when (order.status) {
        "Delivered" -> Color(0xFF2E7D32)
        "Out for Delivery" -> Color(0xFFE65100)
        "Confirmed" -> Color(0xFF1976D2)
        "New" -> Color(0xFF6200EA)
        "Cancelled" -> Color(0xFFD32F2F)
        else -> Color(0xFFF57C00)
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("seller_order_card_${order.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Order ID, Timestamp & Status Dropdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Order #${order.id}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = dateFormat.format(Date(order.timestamp)),
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }

                // Status Dropdown Selector
                ExposedDropdownMenuBox(
                    expanded = menuExpanded,
                    onExpandedChange = { menuExpanded = !menuExpanded }
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = statusColor.copy(alpha = 0.15f),
                        modifier = Modifier
                            .menuAnchor()
                            .clickable { menuExpanded = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = order.status,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = statusColor,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpanded)
                        }
                    }

                    ExposedDropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        statusOptions.forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status) },
                                onClick = {
                                    onStatusChange(status)
                                    menuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(10.dp))

            // Customer Details Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "👤 ${order.customerName}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "📞 ${order.customerPhone}",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        text = "📍 ${order.customerAddress}",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    if (order.deliveryNotes.isNotBlank()) {
                        Text(
                            text = "📝 Note: ${order.deliveryNotes}",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Items Summary
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = order.itemsSummary,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Total Amount & Payment
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total: ${order.formattedTotal}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                Text(
                    text = order.paymentMode,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Customer Contact & Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onCallCustomer,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = "Call Customer", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Call Customer", style = MaterialTheme.typography.labelSmall)
                }

                FilledTonalButton(
                    onClick = onWhatsAppCustomer,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color(0xFF25D366).copy(alpha = 0.15f),
                        contentColor = Color(0xFF1E824C)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp)
                ) {
                    Icon(imageVector = Icons.Outlined.Chat, contentDescription = "WhatsApp", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("WhatsApp", style = MaterialTheme.typography.labelSmall)
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Order",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SellerStoreInfoView() {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("seller_store_info_view"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("🏪", fontSize = 28.sp)
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = OrderCommunicationHelper.SELLER_NAME,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            Text(
                                text = "Fresh Vegetable Store & Mandi Supplier",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Store Address:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "📍 ${OrderCommunicationHelper.SELLER_ADDRESS}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Contact Number:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "📞 ${OrderCommunicationHelper.SELLER_PHONE}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Delivery Coverage:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "🚚 Nawabganj Town, Bijauriya Village, and nearby rural areas of Uttar Pradesh",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { OrderCommunicationHelper.callSeller(context) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Call, contentDescription = "Call")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Test Call")
                        }

                        FilledTonalButton(
                            onClick = { OrderCommunicationHelper.openWhatsAppChat(context) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Outlined.Chat, contentDescription = "WhatsApp")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Test WhatsApp")
                        }
                    }
                }
            }
        }
    }
}
