package com.udemy.groceryshoppingapi.retail.util

import com.udemy.groceryshoppingapi.dto.GroceryItemCreateRequest
import com.udemy.groceryshoppingapi.dto.GroceryItemResponse
import com.udemy.groceryshoppingapi.retail.entity.GroceryItem
import org.springframework.stereotype.Component

@Component
class GroceryItemMapper(private val groceryCategoryMapper: GroceryCategoryMapper) {

    fun toDto(item: GroceryItem) = GroceryItemResponse(
        id = item.id,
        name = item.name,
        category = if (item.groceryCategory != null) groceryCategoryMapper.toDto(item.groceryCategory) else null
    )

    fun toEntity(dto: GroceryItemCreateRequest) = GroceryItem(
        name = dto.name,
        groceryCategory = groceryCategoryMapper.toEntity(dto)
    )
}