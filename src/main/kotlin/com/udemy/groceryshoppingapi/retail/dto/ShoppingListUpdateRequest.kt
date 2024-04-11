package com.udemy.groceryshoppingapi.retail.dto

import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import com.udemy.groceryshoppingapi.retail.entity.Supermarket

data class ShoppingListUpdateRequest(
    var totalAmount: Float?,
    var receiptPictureUrl: String?,
    var isDone: Boolean?,
    var supermarket: Supermarket?,
    val shoppingListItems: List<ShoppingListItem>?
)
