package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.ShoppingListItemCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemUpdateRequest
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem

interface ShoppingListItemService {

    fun createShoppingListItem(
        createRequest: ShoppingListItemCreateRequest,
        shoppingList: ShoppingList?
    ): ShoppingListItem

    fun updateShoppingList(
        shoppingList: ShoppingList,
        shoppingListItems: List<ShoppingListItem>
    ): List<ShoppingListItem>

    fun deleteShoppingListItems(shoppingListItems: List<ShoppingListItem>)

    fun updateShoppingListItems(
        shoppingList: ShoppingList,
        shoppingListItems: List<ShoppingListItemCreateRequest>
    ): List<ShoppingListItem>

    fun getShoppingListItem(id: Long): ShoppingListItem

    fun deleteShoppingListItem(id: Long)

    fun updateShoppingListItem(id: Long, updateRequest: ShoppingListItemUpdateRequest): ShoppingListItem
}