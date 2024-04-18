package com.udemy.groceryshoppingapi.retail.util

import com.udemy.groceryshoppingapi.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import org.springframework.stereotype.Component


@Component
class ShoppingListMapper {
    fun toDto(entity: ShoppingList): ShoppingListResponse {
        val dto = ShoppingListResponse(
            entity.id,
            entity.receiptPictureUrl,
            entity.isDone,
            null, // todo: check this
            emptyList(), // todo: check this
            entity.getTotalAmount()
        )
        return dto
    }

    fun toEntity(request: ShoppingListCreateRequest): ShoppingList {
        val entity = ShoppingList(
            receiptPictureUrl = request.receiptPictureUrl,
            isDone = false,
            supermarket = null, // todo: check this
            shoppingListItems = emptyList() // todo: check this
        )
        return entity
    }
}
