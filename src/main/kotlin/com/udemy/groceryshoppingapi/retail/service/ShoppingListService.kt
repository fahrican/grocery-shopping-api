package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.retail.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.retail.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.user.entity.AppUser

interface ShoppingListService {

    fun createShoppingList(createRequest: ShoppingListCreateRequest, appUser: AppUser): ShoppingListResponse

    fun getShoppingLists(appUser: AppUser): Set<ShoppingListResponse>

    fun getShoppingListById(id: Long, appUser: AppUser): ShoppingListResponse

    fun updateShoppingList(id: Long, updateRequest: ShoppingListUpdateRequest, appUser: AppUser): ShoppingListResponse

    fun deleteShoppingList(id: Long, appUser: AppUser): String
}