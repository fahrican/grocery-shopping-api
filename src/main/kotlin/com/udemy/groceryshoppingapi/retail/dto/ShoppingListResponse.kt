package com.udemy.groceryshoppingapi.retail.dto

import com.udemy.groceryshoppingapi.retail.entity.Supermarket

data class ShoppingListResponse(
    val id: Long,
    var totalAmount: Float,
    var receiptPictureUrl: String? = null,
    var supermarket: Supermarket? = null
)