package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.retail.entity.GroceryItem

interface GroceryItemService {

    fun deleteGroceryItems(items: List<GroceryItem>)
}