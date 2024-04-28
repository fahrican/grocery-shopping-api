package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.GroceryItemCreateRequest
import com.udemy.groceryshoppingapi.dto.GroceryItemResponse
import com.udemy.groceryshoppingapi.dto.GroceryItemUpdateRequest
import com.udemy.groceryshoppingapi.retail.entity.GroceryItem
import com.udemy.groceryshoppingapi.retail.repository.GroceryItemRepository
import com.udemy.groceryshoppingapi.retail.util.GroceryItemMapper
import org.springframework.stereotype.Service

@Service
class GroceryItemServiceImpl(
    private val repository: GroceryItemRepository,
    private val mapper: GroceryItemMapper
) : GroceryItemService {

    override fun deleteGroceryItems(items: List<GroceryItem>) {
        repository.deleteAll(items)
    }

    override fun createGroceryItem(createReq: GroceryItemCreateRequest): GroceryItemResponse {
        val item: GroceryItem = repository.save(
            GroceryItem(
                name = createReq.name,
                category = createReq.category
            )
        )
        return mapper.toDto(item)
    }

    override fun deleteGroceryItem(id: Long) {
        repository.deleteGroceryItem(id)
    }

    override fun updateGroceryItem(grocerId: Long, updateRequest: GroceryItemUpdateRequest): GroceryItemResponse {
        val groceryItem: GroceryItem = repository.findById(grocerId).orElseThrow()
        groceryItem.apply {
            groceryItem.name = updateRequest.name ?: name
            groceryItem.category = updateRequest.category ?: category
        }
        val updatedGrocerItem: GroceryItem = repository.save(groceryItem)
        return mapper.toDto(updatedGrocerItem)
    }
}