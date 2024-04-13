package com.udemy.groceryshoppingapi.retail.util

import com.udemy.groceryshoppingapi.dto.Category
import com.udemy.groceryshoppingapi.dto.GroceryCategoryResponse
import com.udemy.groceryshoppingapi.dto.GroceryItemCreateRequest
import com.udemy.groceryshoppingapi.retail.entity.GroceryCategory
import org.springframework.stereotype.Component

@Component
class GroceryCategoryMapper {
    fun toDto(entity: GroceryCategory) = GroceryCategoryResponse(
        id = entity.id,
        name = entity.category
    )

    fun toEntity(dto: GroceryItemCreateRequest) = GroceryCategory(
        category = dto.groceryCategory.category ?: Category.OTHER
    )
}