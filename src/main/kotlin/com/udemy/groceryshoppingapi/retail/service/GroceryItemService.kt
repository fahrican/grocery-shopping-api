package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.GroceryItemCreateRequest
import com.udemy.groceryshoppingapi.dto.GroceryItemResponse
import com.udemy.groceryshoppingapi.dto.GroceryItemUpdateRequest
import com.udemy.groceryshoppingapi.retail.entity.GroceryItem

interface GroceryItemService {

    fun deleteGroceryItems(items: List<GroceryItem>)

    fun createGroceryItem(createReq: GroceryItemCreateRequest): GroceryItemResponse

    fun deleteGroceryItem(id: Long)

    fun updateGroceryItem(grocerId: Long, updateRequest: GroceryItemUpdateRequest): GroceryItemResponse
}