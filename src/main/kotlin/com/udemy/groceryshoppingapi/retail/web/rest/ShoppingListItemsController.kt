package com.udemy.groceryshoppingapi.retail.web.rest

import com.udemy.groceryshoppingapi.api.ShoppingListItemResource
import com.udemy.groceryshoppingapi.auth.service.ClientSessionService
import com.udemy.groceryshoppingapi.dto.ShoppingListItemCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemResponse
import com.udemy.groceryshoppingapi.dto.ShoppingListItemUpdateRequest
import com.udemy.groceryshoppingapi.retail.service.ShoppingListService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ShoppingListItemsController(
    private val shoppingListService: ShoppingListService,
    private val userProvider: ClientSessionService
) : ShoppingListItemResource {

    override fun getShoppingListItems(listId: Long): ResponseEntity<List<ShoppingListItemResponse>> {
        val shoppingListItems: List<ShoppingListItemResponse> =
            shoppingListService.getShoppingListItems(listId, userProvider.getAuthenticatedUser()).toList()
        return ResponseEntity.ok(shoppingListItems)
    }

    override fun createShoppingListItem(
        listId: Long,
        shoppingListItemCreateRequest: ShoppingListItemCreateRequest
    ): ResponseEntity<ShoppingListItemResponse> {
        return super.createShoppingListItem(listId, shoppingListItemCreateRequest)
    }

    override fun deleteShoppingListItem(listId: Long, itemId: Long): ResponseEntity<Unit> {
        return super.deleteShoppingListItem(listId, itemId)
    }

    override fun getShoppingListItem(listId: Long, itemId: Long): ResponseEntity<ShoppingListItemResponse> =
        ResponseEntity.ok(shoppingListService.getShoppingListItem(listId, itemId, userProvider.getAuthenticatedUser()))

    override fun updateShoppingListItem(
        listId: Long,
        itemId: Long,
        shoppingListItemUpdateRequest: ShoppingListItemUpdateRequest
    ): ResponseEntity<ShoppingListItemResponse> {
        return super.updateShoppingListItem(listId, itemId, shoppingListItemUpdateRequest)
    }
}