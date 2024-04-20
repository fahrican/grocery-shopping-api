package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.GroceryItemCreateRequest
import com.udemy.groceryshoppingapi.retail.entity.GroceryItem
import com.udemy.groceryshoppingapi.retail.repository.GroceryItemRepository
import com.udemy.groceryshoppingapi.user.entity.AppUser
import org.springframework.stereotype.Service

@Service
class GroceryItemServiceImpl(private val repository: GroceryItemRepository) : GroceryItemService {
    override fun createGroceryItem(createRequest: GroceryItemCreateRequest, appUser: AppUser): GroceryItem {
        val groceryItem = GroceryItem(name = createRequest.name, category = createRequest.category)
        return repository.save(groceryItem)
    }
}