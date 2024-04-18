package com.udemy.groceryshoppingapi.retail.util

import com.udemy.groceryshoppingapi.dto.ShoppingListItemCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemResponse
import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import org.springframework.stereotype.Component


@Component
class ShoppingListItemMapper(
    private val groceryItemMapper: GroceryItemMapper
) {

    fun toDto(entity: ShoppingListItem?): ShoppingListItemResponse {
        val groceryItemResponse = groceryItemMapper.toDto(entity?.groceryItem!!)
        val dto = ShoppingListItemResponse(
            entity.id,
            entity.quantity,
            entity.price,
            null,  // todo: check this
            groceryItemResponse
        )
        return dto
    }

    fun toEntity(request: ShoppingListItemCreateRequest): ShoppingListItem {
        val groceryItemEntity = groceryItemMapper.toEntity(request.groceryItem)
        val entity = ShoppingListItem(
            quantity = request.quantity,
            price = request.price,
            shoppingList = null, // todo: check this
            groceryItem = groceryItemEntity
        )
        return entity
    }
}