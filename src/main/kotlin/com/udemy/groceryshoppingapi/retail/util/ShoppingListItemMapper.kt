package com.udemy.groceryshoppingapi.retail.util

import com.udemy.groceryshoppingapi.dto.ShoppingListItemCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemResponse
import com.udemy.groceryshoppingapi.retail.entity.GroceryItem
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
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
            groceryItemResponse
        )
        return dto
    }

    fun toEntity(
        request: ShoppingListItemCreateRequest,
        shoppingList: ShoppingList?,
        groceryItemEntity: GroceryItem
    ): ShoppingListItem {
        val entity = ShoppingListItem(
            quantity = request.quantity,
            price = request.price,
            shoppingList = shoppingList, // todo: check this
            groceryItem = groceryItemEntity
        )
        return entity
    }
}