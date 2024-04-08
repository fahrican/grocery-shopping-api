package com.udemy.groceryshoppingapi.retail.util

import com.udemy.groceryshoppingapi.retail.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.retail.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.user.entity.AppUser
import org.springframework.stereotype.Component

@Component
class ShoppingListMapper {

    fun toDto(entity: ShoppingList): ShoppingListResponse {
        return ShoppingListResponse(
            id = entity.id,
            totalAmount = entity.totalAmount,
            receiptPictureUrl = entity.receiptPictureUrl,
            supermarket = entity.supermarket,
            shoppingListItems = entity.shoppingListItems
        )
    }

    fun toEntity(dto: ShoppingListCreateRequest, appUser: AppUser): ShoppingList {
        val entity = ShoppingList()
        entity.totalAmount = dto.totalAmount ?: 0.0f
        entity.receiptPictureUrl = dto.receiptPictureUrl
        entity.supermarket = dto.supermarket
        entity.shoppingListItems = dto.shoppingListItems
        entity.appUser = appUser
        return entity
    }
}
