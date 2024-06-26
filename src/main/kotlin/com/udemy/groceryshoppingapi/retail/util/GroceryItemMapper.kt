package com.udemy.groceryshoppingapi.retail.util

import com.udemy.groceryshoppingapi.dto.GroceryItemCreateRequest
import com.udemy.groceryshoppingapi.dto.GroceryItemResponse
import com.udemy.groceryshoppingapi.retail.entity.GroceryItem
import org.springframework.stereotype.Component

@Component
class GroceryItemMapper {

    fun toDto(item: GroceryItem) = GroceryItemResponse(
        id = item.id,
        name = item.name,
        category = item.category
    )

    fun toEntity(dto: GroceryItemCreateRequest) = GroceryItem(
        name = dto.name,
        category = dto.category
    )
}