package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.ShoppingListItemCreateRequest
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import com.udemy.groceryshoppingapi.user.entity.AppUser

interface ShoppingListItemService {

    fun createShoppingListItem(
        createRequest: ShoppingListItemCreateRequest,
        appUser: AppUser,
        shoppingList: ShoppingList?
    ): ShoppingListItem

    /*    fun deleteShoppingListItem(shoppingListId: Long, shoppingListItemId: Long)
        fun getShoppingListItems(shoppingListId: Long): List<ShoppingListItemResponse>
        fun getShoppingListItem(shoppingListId: Long, shoppingListItemId: Long): ShoppingListItemResponse*/
}