package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.CartCheckoutScreen
import com.example.ui.screens.CustomerHomeScreen
import com.example.ui.screens.MyOrdersScreen
import com.example.ui.screens.OrderSuccessScreen
import com.example.ui.screens.SellerDashboardScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppMode
import com.example.ui.viewmodel.CustomerTab
import com.example.ui.viewmodel.VegetableViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: VegetableViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(
    viewModel: VegetableViewModel,
    modifier: Modifier = Modifier
) {
    val currentMode by viewModel.currentMode.collectAsStateWithLifecycle()
    val customerTab by viewModel.customerTab.collectAsStateWithLifecycle()
    val cartItemCount by viewModel.cartItemCount.collectAsStateWithLifecycle()
    val lastPlacedOrder by viewModel.lastPlacedOrder.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F4)),
        contentAlignment = Alignment.TopCenter
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 440.dp)
                .background(MaterialTheme.colorScheme.background),
            bottomBar = {
                // Show customer bottom navigation bar only when in Customer Mode and no active order success screen
                if (currentMode == AppMode.CUSTOMER && lastPlacedOrder == null) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("customer_bottom_navigation")
                    ) {
                        NavigationBarItem(
                            selected = customerTab == CustomerTab.SHOP,
                            onClick = { viewModel.setCustomerTab(CustomerTab.SHOP) },
                            icon = {
                                Icon(
                                    imageVector = if (customerTab == CustomerTab.SHOP) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                                    contentDescription = "Shop"
                                )
                            },
                            label = {
                                Text(
                                    text = "Fresh Veggies",
                                    fontWeight = if (customerTab == CustomerTab.SHOP) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.testTag("nav_item_shop")
                        )

                        NavigationBarItem(
                            selected = customerTab == CustomerTab.CART,
                            onClick = { viewModel.setCustomerTab(CustomerTab.CART) },
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (cartItemCount > 0) {
                                            Badge(
                                                containerColor = MaterialTheme.colorScheme.error,
                                                contentColor = MaterialTheme.colorScheme.onError
                                            ) {
                                                Text("$cartItemCount")
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (customerTab == CustomerTab.CART) Icons.Filled.ShoppingCart else Icons.Outlined.ShoppingCart,
                                        contentDescription = "Cart"
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = "Cart",
                                    fontWeight = if (customerTab == CustomerTab.CART) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.testTag("nav_item_cart")
                        )

                        NavigationBarItem(
                            selected = customerTab == CustomerTab.MY_ORDERS,
                            onClick = { viewModel.setCustomerTab(CustomerTab.MY_ORDERS) },
                            icon = {
                                Icon(
                                    imageVector = if (customerTab == CustomerTab.MY_ORDERS) Icons.Filled.ListAlt else Icons.Outlined.ListAlt,
                                    contentDescription = "My Orders"
                                )
                            },
                            label = {
                                Text(
                                    text = "My Orders",
                                    fontWeight = if (customerTab == CustomerTab.MY_ORDERS) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.testTag("nav_item_orders")
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = Pair(currentMode, lastPlacedOrder != null),
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "AppScreenTransition"
                ) { (mode, hasPlacedOrder) ->
                    when {
                        hasPlacedOrder && lastPlacedOrder != null -> {
                            OrderSuccessScreen(
                                order = lastPlacedOrder!!,
                                onContinueShopping = {
                                    viewModel.dismissLastPlacedOrder()
                                    viewModel.setCustomerTab(CustomerTab.SHOP)
                                },
                                onViewMyOrders = {
                                    viewModel.dismissLastPlacedOrder()
                                    viewModel.setCustomerTab(CustomerTab.MY_ORDERS)
                                }
                            )
                        }
                        mode == AppMode.SELLER -> {
                            SellerDashboardScreen(viewModel = viewModel)
                        }
                        else -> {
                            when (customerTab) {
                                CustomerTab.SHOP -> CustomerHomeScreen(viewModel = viewModel)
                                CustomerTab.CART -> CartCheckoutScreen(viewModel = viewModel)
                                CustomerTab.MY_ORDERS -> MyOrdersScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
