package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.retail.entity.GroceryItem
import com.udemy.groceryshoppingapi.retail.repository.GroceryItemRepository
import org.springframework.stereotype.Service

@Service
class GroceryItemServiceImpl(private val repository: GroceryItemRepository) : GroceryItemService {
    override fun deleteGroceryItems(items: List<GroceryItem>) {
        repository.deleteAll(items)
    }
}