package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.GroceryItemCreateRequest
import com.udemy.groceryshoppingapi.retail.entity.GroceryItem
import com.udemy.groceryshoppingapi.user.entity.AppUser

interface GroceryItemService {

    fun createGroceryItem(createRequest: GroceryItemCreateRequest, appUser: AppUser): GroceryItem
}