package com.udemy.groceryshoppingapi.retail.util

import com.udemy.groceryshoppingapi.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.entity.Supermarket
import com.udemy.groceryshoppingapi.user.entity.AppUser
import org.springframework.stereotype.Component

@Component
class ShoppingListMapper(
    private val supermarketMapper: SupermarketMapper,
    private val shoppingListItemMapper: ShoppingListItemMapper
) {

    fun toDto(entity: ShoppingList?): ShoppingListResponse {
        val supermarket = entity?.supermarket ?: Supermarket()
        return ShoppingListResponse(
            id = entity?.id,
            totalAmount = entity?.getTotalAmount(),
            receiptPictureUrl = entity?.receiptPictureUrl,
            supermarket = supermarketMapper.toDto(supermarket),
            shoppingListItems = entity?.shoppingListItems?.map { shoppingListItemMapper.toDto(it) },
        )
    }

    fun toEntity(dto: ShoppingListCreateRequest, appUser: AppUser): ShoppingList {
        val entity = ShoppingList()
        entity.receiptPictureUrl = dto.receiptPictureUrl
        entity.supermarket = supermarketMapper.toEntity(dto.supermarket, appUser)
        entity.shoppingListItems = dto.shoppingListItems.map { shoppingListItemMapper.toEntity(it, appUser) }
        entity.appUser = appUser
        return entity
    }
}
