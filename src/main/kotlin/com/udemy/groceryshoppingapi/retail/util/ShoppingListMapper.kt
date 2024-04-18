package com.udemy.groceryshoppingapi.retail.util

import com.udemy.groceryshoppingapi.dto.Hypermarket
import com.udemy.groceryshoppingapi.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.entity.Supermarket
import com.udemy.groceryshoppingapi.user.entity.AppUser
import org.springframework.stereotype.Component

@Component
class ShoppingListMapper {

    fun toDto(
        entity: ShoppingList,
        supermarketMapper: SupermarketMapper,
        shoppingListItemMapper: ShoppingListItemMapper
    ): ShoppingListResponse {
        val supermarket = entity.supermarket ?: Supermarket(name = Hypermarket.OTHER)
        return ShoppingListResponse(
            id = entity.id,
            receiptPictureUrl = entity.receiptPictureUrl,
            isDone = entity.isDone,
            supermarket = supermarketMapper.toDto(supermarket),
            shoppingListItems = entity.shoppingListItems.map { shoppingListItemMapper.toDto(it) },
            totalAmount = entity.getTotalAmount()
        )
    }

    fun toEntity(
        request: ShoppingListCreateRequest,
        user: AppUser,
        supermarket: Supermarket,
        shoppingListItemMapper: ShoppingListItemMapper
    ) = ShoppingList(
        receiptPictureUrl = request.receiptPictureUrl,
        appUser = user,
        supermarket = supermarket,
        shoppingListItems = request.shoppingListItems.map { shoppingListItemMapper.toEntity(it) }
    )
}