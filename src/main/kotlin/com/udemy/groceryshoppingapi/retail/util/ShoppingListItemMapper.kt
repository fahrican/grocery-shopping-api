package com.udemy.groceryshoppingapi.retail.util

import com.udemy.groceryshoppingapi.dto.ShoppingListItemCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemResponse
import com.udemy.groceryshoppingapi.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import org.springframework.stereotype.Component


@Component
class ShoppingListItemMapper(
    private val groceryItemMapper: GroceryItemMapper
) {

    fun toDto(entity: ShoppingListItem?, shoppingListResponse: ShoppingListResponse?): ShoppingListItemResponse {
        val groceryItemResponse = groceryItemMapper.toDto(entity?.groceryItem!!)
        val dto = ShoppingListItemResponse(
            entity.id,
            entity.quantity,
            entity.price,
            shoppingListResponse,  // todo: check this
            groceryItemResponse
        )
        return dto
    }

    fun toEntity(request: ShoppingListItemCreateRequest, shoppingList: ShoppingList?): ShoppingListItem {
        val groceryItemEntity = groceryItemMapper.toEntity(request.groceryItem)
        val entity = ShoppingListItem(
            quantity = request.quantity,
            price = request.price,
            shoppingList = shoppingList, // todo: check this
            groceryItem = groceryItemEntity
        )
        return entity
    }
}