package com.udemy.groceryshoppingapi.retail.dto

import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import com.udemy.groceryshoppingapi.retail.entity.Supermarket

data class ShoppingListResponse(
    val id: Long,
    var receiptPictureUrl: String? = null,
    var isDone: Boolean = false,
    var supermarket: Supermarket? = null,
    val shoppingListItems: List<ShoppingListItem>,
    var totalAmount: Float? = null,
)