package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.GroceryItemResponse
import com.udemy.groceryshoppingapi.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.dto.ShoppingListUpdateRequest
import com.udemy.groceryshoppingapi.user.entity.AppUser

interface ShoppingListService {

    fun createShoppingList(createRequest: ShoppingListCreateRequest, appUser: AppUser): ShoppingListResponse

    fun getShoppingListById(id: Long, appUser: AppUser): ShoppingListResponse

    fun getShoppingLists(appUser: AppUser, isDone: Boolean?): Set<ShoppingListResponse>

    fun updateShoppingList(id: Long, updateRequest: ShoppingListUpdateRequest, appUser: AppUser): ShoppingListResponse

    fun deleteShoppingList(id: Long, appUser: AppUser)

    fun getGroceryItem(shoppingListId: Long, shoppingListItemId: Long, appUser: AppUser): GroceryItemResponse
}