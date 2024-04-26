package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.GroceryItemResponse
import com.udemy.groceryshoppingapi.dto.GroceryItemUpdateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemResponse
import com.udemy.groceryshoppingapi.dto.ShoppingListItemUpdateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.dto.ShoppingListUpdateRequest
import com.udemy.groceryshoppingapi.user.entity.AppUser

interface ShoppingListService {

    fun createShoppingList(createRequest: ShoppingListCreateRequest, appUser: AppUser): ShoppingListResponse

    fun getShoppingListById(id: Long, appUser: AppUser): ShoppingListResponse

    fun getShoppingLists(appUser: AppUser, isDone: Boolean?): Set<ShoppingListResponse>

    fun updateShoppingList(id: Long, updateRequest: ShoppingListUpdateRequest, appUser: AppUser): ShoppingListResponse

    fun deleteShoppingList(id: Long, appUser: AppUser)

    fun getGroceryItem(listId: Long, listItemId: Long, appUser: AppUser): GroceryItemResponse

    fun updateGroceryItem(grocerId: Long, updateRequest: GroceryItemUpdateRequest): GroceryItemResponse

    fun getShoppingListItems(listId: Long, appUser: AppUser): Set<ShoppingListItemResponse>

    fun getShoppingListItem(listId: Long, itemId: Long, appUser: AppUser): ShoppingListItemResponse

    fun createShoppingListItem(
        listId: Long,
        createRequest: ShoppingListItemCreateRequest,
        appUser: AppUser
    ): ShoppingListItemResponse

    fun deleteShoppingListItem(listId: Long, itemId: Long, appUser: AppUser)

    fun updateShoppingListItem(
        listId: Long,
        itemId: Long,
        updateRequest: ShoppingListItemUpdateRequest,
        appUser: AppUser
    ): ShoppingListItemResponse
}