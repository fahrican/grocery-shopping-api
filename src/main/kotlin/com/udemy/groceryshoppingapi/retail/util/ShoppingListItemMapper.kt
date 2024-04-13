package com.udemy.groceryshoppingapi.retail.util

import com.udemy.groceryshoppingapi.dto.ShoppingListItemCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemResponse
import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import com.udemy.groceryshoppingapi.user.entity.AppUser
import org.springframework.stereotype.Component

@Component
class ShoppingListItemMapper(
    private val shoppingListMapper: ShoppingListMapper,
    private val groceryItemMapper: GroceryItemMapper
) {

    fun toDto(entity: ShoppingListItem): ShoppingListItemResponse {
        return ShoppingListItemResponse(
            id = entity.id,
            quantity = entity.quantity,
            price = entity.price,
            shoppingList = shoppingListMapper.toDto(entity.shoppingList),
            groceryItem = if (entity.groceryItem != null) groceryItemMapper.toDto(entity.groceryItem!!) else null
        )
    }

    fun toEntity(dto: ShoppingListItemCreateRequest, appUser: AppUser): ShoppingListItem {
        return ShoppingListItem(
            quantity = dto.quantity,
            price = dto.price,
            shoppingList = shoppingListMapper.toEntity(dto.shoppingList, appUser),
            groceryItem = groceryItemMapper.toEntity(dto.groceryItem)
        )
    }
}