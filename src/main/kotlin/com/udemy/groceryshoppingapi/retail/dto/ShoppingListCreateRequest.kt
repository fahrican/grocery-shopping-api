package com.udemy.groceryshoppingapi.retail.dto

import com.udemy.groceryshoppingapi.retail.entity.Supermarket

data class ShoppingListCreateRequest(
    var totalAmount: Float,
    var receiptPictureUrl: String? = null,
    var supermarket: Supermarket
)
