package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.data.model.CartItem
import com.example.data.model.CustomerOrder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object OrderCommunicationHelper {

    const val SELLER_PHONE = "7457870806"
    const val SELLER_PHONE_INTL = "+917457870806"
    const val SELLER_NAME = "Vedanshi Fresh Vegetables"
    const val SELLER_ADDRESS = "Nawabganj, Bijauriya, Uttar Pradesh"

    /**
     * Initiate a phone call to the seller.
     */
    fun callSeller(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$SELLER_PHONE")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open dialer: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Initiate a phone call to a customer (used by seller).
     */
    fun callCustomer(context: Context, phoneNumber: String) {
        try {
            val cleanNumber = phoneNumber.replace(Regex("[^0-9+]"), "")
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$cleanNumber")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open dialer: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Open WhatsApp with a pre-formatted message to the seller.
     */
    fun openWhatsAppChat(context: Context, message: String = "Hello Vedanshi Fresh Vegetables! I want to order fresh vegetables.") {
        try {
            val encodedMsg = URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=91$SELLER_PHONE&text=$encodedMsg")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                val waUri = Uri.parse("whatsapp://send?phone=91$SELLER_PHONE&text=${URLEncoder.encode(message, StandardCharsets.UTF_8.toString())}")
                val waIntent = Intent(Intent.ACTION_VIEW, waUri).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(waIntent)
            } catch (e2: Exception) {
                Toast.makeText(context, "WhatsApp could not be opened: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Open WhatsApp with a pre-formatted order summary from Cart.
     */
    fun sendCartOrderToWhatsApp(
        context: Context,
        customerName: String,
        customerPhone: String,
        customerAddress: String,
        deliveryNotes: String,
        cartItems: List<CartItem>,
        totalAmount: Double,
        paymentMode: String
    ) {
        val nameDisplay = if (customerName.isNotBlank()) customerName else "Valued Customer"
        val addressDisplay = if (customerAddress.isNotBlank()) customerAddress else "To be shared in chat"
        val phoneDisplay = if (customerPhone.isNotBlank()) customerPhone else "WhatsApp Chat"

        val itemsListStr = cartItems.joinToString("\n") { item ->
            "• ${item.vegetable.name} (${item.vegetable.hindiName}): *${item.weightDisplay}* - ${item.formattedPrice}"
        }

        val formattedTotal = if (totalAmount % 1.0 == 0.0) totalAmount.toInt().toString() else String.format("%.1f", totalAmount)

        val message = """
            🥬 *NEW VEGETABLE ORDER - VEDANSHI FRESH* 🥬
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            👤 *Customer Name:* $nameDisplay
            📞 *Contact:* $phoneDisplay
            📍 *Delivery Address:* $addressDisplay
            ${if (deliveryNotes.isNotBlank()) "📝 *Delivery Notes:* $deliveryNotes\n" else ""}
            🛒 *SELECTED VEGETABLES (${cartItems.size} items):*
            $itemsListStr
            
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            💰 *Total Bill Amount:* ₹$formattedTotal
            💳 *Payment Mode:* $paymentMode
            
            📍 *Store:* $SELLER_NAME ($SELLER_ADDRESS)
            📞 *Contact:* $SELLER_PHONE
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            _Please confirm my vegetable order and delivery time._
        """.trimIndent()

        openWhatsAppChat(context, message)
    }

    /**
     * Open WhatsApp to send an existing placed order details to seller or customer.
     */
    fun sendOrderDetailsToWhatsApp(context: Context, order: CustomerOrder, recipientPhone: String = SELLER_PHONE) {
        val targetPhone = recipientPhone.replace(Regex("[^0-9]"), "")
        val formattedTarget = if (targetPhone.startsWith("91")) targetPhone else "91$targetPhone"

        val message = """
            🥬 *ORDER #${order.id} - VEDANSHI FRESH VEGETABLES* 🥬
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            👤 *Customer:* ${order.customerName}
            📞 *Phone:* ${order.customerPhone}
            📍 *Address:* ${order.customerAddress}
            📌 *Status:* ${order.status}
            
            🛒 *ITEMS:*
            ${order.itemsSummary}
            
            💰 *Total Amount:* ${order.formattedTotal}
            💳 *Payment:* ${order.paymentMode}
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            _Vedanshi Fresh Vegetables - Nawabganj, Bijauriya, UP_
        """.trimIndent()

        try {
            val encodedMsg = URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
            val uri = Uri.parse("https://wa.me/$formattedTarget?text=$encodedMsg")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open WhatsApp: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
