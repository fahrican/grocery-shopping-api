package com.udemy.groceryshoppingapi.retail.util

import com.udemy.groceryshoppingapi.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemResponse
import com.udemy.groceryshoppingapi.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.dto.SupermarketResponse
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import com.udemy.groceryshoppingapi.retail.entity.Supermarket
import com.udemy.groceryshoppingapi.user.entity.AppUser
import org.springframework.stereotype.Component


@Component
class ShoppingListMapper {
    fun toDto(
        entity: ShoppingList,
        supermarket: SupermarketResponse,
        shoppingListItems: List<ShoppingListItemResponse>
    ): ShoppingListResponse {
        val dto = ShoppingListResponse(
            entity.id,
            entity.receiptPictureUrl,
            entity.isDone,
            supermarket = supermarket, // todo: check this
            shoppingListItems = shoppingListItems, // todo: check this
            entity.getTotalAmount()
        )
        return dto
    }

    fun toEntity(
        request: ShoppingListCreateRequest,
        supermarket: Supermarket,
        shoppingListItems: List<ShoppingListItem>,
        appUser: AppUser
    ): ShoppingList {
        val entity = ShoppingList(
            receiptPictureUrl = request.receiptPictureUrl,
            isDone = false,
            supermarket = supermarket, // todo: check this
            shoppingListItems = shoppingListItems, // todo: check this
            appUser = appUser
        )
        return entity
    }
}
