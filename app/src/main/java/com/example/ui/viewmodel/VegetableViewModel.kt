package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.CartItem
import com.example.data.model.CustomerOrder
import com.example.data.model.Vegetable
import com.example.data.repository.OrderRepository
import com.example.data.repository.VegetableRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppMode {
    CUSTOMER,
    SELLER
}

enum class CustomerTab {
    SHOP,
    CART,
    MY_ORDERS
}

enum class SellerTab {
    INVENTORY,
    ORDERS,
    STORE_INFO
}

data class CustomerCheckoutInfo(
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val deliveryNotes: String = "",
    val paymentMode: String = "Cash on Delivery (COD)"
)

class VegetableViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application, viewModelScope)
    private val vegetableRepository = VegetableRepository(db.vegetableDao())
    private val orderRepository = OrderRepository(db.orderDao())

    // App Navigation State
    private val _currentMode = MutableStateFlow(AppMode.CUSTOMER)
    val currentMode: StateFlow<AppMode> = _currentMode.asStateFlow()

    private val _customerTab = MutableStateFlow(CustomerTab.SHOP)
    val customerTab: StateFlow<CustomerTab> = _customerTab.asStateFlow()

    private val _sellerTab = MutableStateFlow(SellerTab.INVENTORY)
    val sellerTab: StateFlow<SellerTab> = _sellerTab.asStateFlow()

    // Filter & Search State
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Raw Vegetables Stream
    val allVegetables: StateFlow<List<Vegetable>> = vegetableRepository.allVegetables
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Filtered Vegetables for Customer & Seller
    val filteredVegetables: StateFlow<List<Vegetable>> = combine(
        allVegetables,
        _selectedCategory,
        _searchQuery
    ) { veggies, category, query ->
        veggies.filter { veg ->
            val matchesCategory = (category == "All" || veg.category.equals(category, ignoreCase = true))
            val matchesQuery = query.isBlank() ||
                    veg.name.contains(query, ignoreCase = true) ||
                    veg.hindiName.contains(query, ignoreCase = true) ||
                    veg.category.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Cart State
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    val cartTotalAmount: StateFlow<Double> = _cartItems.combine(_cartItems) { items, _ ->
        items.sumOf { it.totalPrice }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val cartItemCount: StateFlow<Int> = _cartItems.combine(_cartItems) { items, _ ->
        items.size
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Checkout Info State
    private val _checkoutInfo = MutableStateFlow(CustomerCheckoutInfo())
    val checkoutInfo: StateFlow<CustomerCheckoutInfo> = _checkoutInfo.asStateFlow()

    // Placed Orders Stream
    val allOrders: StateFlow<List<CustomerOrder>> = orderRepository.allOrders
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Order status filter in Seller mode
    private val _sellerOrderStatusFilter = MutableStateFlow("All")
    val sellerOrderStatusFilter: StateFlow<String> = _sellerOrderStatusFilter.asStateFlow()

    val filteredSellerOrders: StateFlow<List<CustomerOrder>> = combine(
        allOrders,
        _sellerOrderStatusFilter
    ) { orders, filter ->
        if (filter == "All") orders else orders.filter { it.status.equals(filter, ignoreCase = true) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Dialog state for adding/editing vegetable
    private val _editingVegetable = MutableStateFlow<Vegetable?>(null)
    val editingVegetable: StateFlow<Vegetable?> = _editingVegetable.asStateFlow()

    private val _isAddEditDialogVisible = MutableStateFlow(false)
    val isAddEditDialogVisible: StateFlow<Boolean> = _isAddEditDialogVisible.asStateFlow()

    // Last Placed Order (for confirmation banner / modal)
    private val _lastPlacedOrder = MutableStateFlow<CustomerOrder?>(null)
    val lastPlacedOrder: StateFlow<CustomerOrder?> = _lastPlacedOrder.asStateFlow()

    init {
        viewModelScope.launch {
            vegetableRepository.checkAndSeedInitialData()
        }
    }

    // Navigation setters
    fun setMode(mode: AppMode) {
        _currentMode.value = mode
    }

    fun setCustomerTab(tab: CustomerTab) {
        _customerTab.value = tab
    }

    fun setSellerTab(tab: SellerTab) {
        _sellerTab.value = tab
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSellerOrderStatusFilter(filter: String) {
        _sellerOrderStatusFilter.value = filter
    }

    // Cart Actions
    fun addToCart(vegetable: Vegetable, weightKg: Double) {
        _cartItems.update { currentList ->
            val existingIndex = currentList.indexOfFirst { it.vegetable.id == vegetable.id }
            if (existingIndex >= 0) {
                // Update weight
                val updated = currentList.toMutableList()
                val existing = updated[existingIndex]
                updated[existingIndex] = existing.copy(weightKg = existing.weightKg + weightKg)
                updated
            } else {
                currentList + CartItem(vegetable = vegetable, weightKg = weightKg)
            }
        }
    }

    fun setCartItemWeight(vegetableId: Long, weightKg: Double) {
        if (weightKg <= 0.0) {
            removeFromCart(vegetableId)
            return
        }
        _cartItems.update { currentList ->
            currentList.map { item ->
                if (item.vegetable.id == vegetableId) {
                    item.copy(weightKg = weightKg)
                } else {
                    item
                }
            }
        }
    }

    fun removeFromCart(vegetableId: Long) {
        _cartItems.update { currentList ->
            currentList.filterNot { it.vegetable.id == vegetableId }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    // Checkout Form
    fun updateCheckoutInfo(
        name: String? = null,
        phone: String? = null,
        address: String? = null,
        notes: String? = null,
        paymentMode: String? = null
    ) {
        _checkoutInfo.update { current ->
            current.copy(
                name = name ?: current.name,
                phone = phone ?: current.phone,
                address = address ?: current.address,
                deliveryNotes = notes ?: current.deliveryNotes,
                paymentMode = paymentMode ?: current.paymentMode
            )
        }
    }

    fun placeOrder(onSuccess: (CustomerOrder) -> Unit) {
        val currentCart = _cartItems.value
        if (currentCart.isEmpty()) return

        val info = _checkoutInfo.value
        val itemsSummary = currentCart.joinToString("\n") { item ->
            "• ${item.vegetable.name} (${item.vegetable.hindiName}): ${item.weightDisplay} = ${item.formattedPrice}"
        }
        val total = currentCart.sumOf { it.totalPrice }

        val order = CustomerOrder(
            customerName = info.name.trim().ifEmpty { "Valued Customer" },
            customerPhone = info.phone.trim(),
            customerAddress = info.address.trim().ifEmpty { "Nawabganj / Bijauriya Local" },
            deliveryNotes = info.deliveryNotes.trim(),
            itemsSummary = itemsSummary,
            totalAmount = total,
            totalItemCount = currentCart.size,
            status = "New",
            timestamp = System.currentTimeMillis(),
            paymentMode = info.paymentMode
        )

        viewModelScope.launch {
            val orderId = orderRepository.insertOrder(order)
            val savedOrder = order.copy(id = orderId)

            // Also dual-save to SharedPreferences with key "vedanshi_orders"
            com.example.util.OrdersStorageHelper.saveOrderToLocalStorage(
                context = getApplication(),
                orderId = orderId,
                name = savedOrder.customerName,
                mobile = savedOrder.customerPhone,
                address = savedOrder.customerAddress,
                cartItems = currentCart,
                totalAmount = total,
                status = "New"
            )

            _lastPlacedOrder.value = savedOrder
            clearCart()
            onSuccess(savedOrder)
        }
    }

    fun dismissLastPlacedOrder() {
        _lastPlacedOrder.value = null
    }

    // Seller Product Management
    fun openAddVegetableDialog() {
        _editingVegetable.value = null
        _isAddEditDialogVisible.value = true
    }

    fun openEditVegetableDialog(vegetable: Vegetable) {
        _editingVegetable.value = vegetable
        _isAddEditDialogVisible.value = true
    }

    fun closeAddEditDialog() {
        _editingVegetable.value = null
        _isAddEditDialogVisible.value = false
    }

    fun saveVegetable(vegetable: Vegetable) {
        viewModelScope.launch {
            if (vegetable.id == 0L) {
                vegetableRepository.insertVegetable(vegetable)
            } else {
                vegetableRepository.updateVegetable(vegetable)
            }
            closeAddEditDialog()
        }
    }

    fun toggleStockStatus(vegetable: Vegetable) {
        viewModelScope.launch {
            vegetableRepository.updateStockStatus(vegetable.id, !vegetable.isInStock)
        }
    }

    fun updateVegetablePrice(vegetableId: Long, newPrice: Double) {
        viewModelScope.launch {
            vegetableRepository.updatePrice(vegetableId, newPrice)
        }
    }

    fun deleteVegetable(vegetable: Vegetable) {
        viewModelScope.launch {
            vegetableRepository.deleteVegetable(vegetable)
        }
    }

    // Seller Order Management
    fun updateOrderStatus(orderId: Long, newStatus: String) {
        viewModelScope.launch {
            orderRepository.updateOrderStatus(orderId, newStatus)
        }
    }

    fun deleteOrder(order: CustomerOrder) {
        viewModelScope.launch {
            orderRepository.deleteOrder(order)
        }
    }
}
