package com.udemy.groceryshoppingapi.retail.dto

import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import com.udemy.groceryshoppingapi.retail.entity.Supermarket

data class ShoppingListCreateRequest(
    var receiptPictureUrl: String? = null,
    var supermarket: Supermarket,
    val shoppingListItems: List<ShoppingListItem>
)
