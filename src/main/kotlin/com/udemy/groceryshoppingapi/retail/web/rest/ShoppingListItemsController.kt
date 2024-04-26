package com.udemy.groceryshoppingapi.retail.web.rest

import com.udemy.groceryshoppingapi.api.ShoppingListItemResource
import com.udemy.groceryshoppingapi.auth.service.ClientSessionService
import com.udemy.groceryshoppingapi.dto.ShoppingListItemCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemResponse
import com.udemy.groceryshoppingapi.dto.ShoppingListItemUpdateRequest
import com.udemy.groceryshoppingapi.retail.service.ShoppingListService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ShoppingListItemsController(
    private val service: ShoppingListService,
    private val userProvider: ClientSessionService
) : ShoppingListItemResource {

    override fun getShoppingListItems(listId: Long): ResponseEntity<List<ShoppingListItemResponse>> {
        val shoppingListItems: List<ShoppingListItemResponse> =
            service.getShoppingListItems(listId, userProvider.getAuthenticatedUser()).toList()
        return ResponseEntity.ok(shoppingListItems)
    }

    override fun createShoppingListItem(
        listId: Long,
        shoppingListItemCreateRequest: ShoppingListItemCreateRequest
    ): ResponseEntity<ShoppingListItemResponse> {
        val shoppingListItem = service.createShoppingListItem(
            listId,
            shoppingListItemCreateRequest,
            userProvider.getAuthenticatedUser()
        )
        return ResponseEntity(shoppingListItem, HttpStatus.CREATED)
    }

    override fun deleteShoppingListItem(listId: Long, itemId: Long): ResponseEntity<Unit> {
        service.deleteShoppingListItem(listId, itemId, userProvider.getAuthenticatedUser())
        return ResponseEntity(Unit, HttpStatus.NO_CONTENT)
    }

    override fun getShoppingListItem(listId: Long, itemId: Long): ResponseEntity<ShoppingListItemResponse> =
        ResponseEntity.ok(service.getShoppingListItem(listId, itemId, userProvider.getAuthenticatedUser()))

    override fun updateShoppingListItem(
        listId: Long,
        itemId: Long,
        shoppingListItemUpdateRequest: ShoppingListItemUpdateRequest
    ): ResponseEntity<ShoppingListItemResponse> {
        val shoppingListItemResponse =
            service.updateShoppingListItem(
                listId,
                itemId,
                shoppingListItemUpdateRequest,
                userProvider.getAuthenticatedUser()
            )
        return ResponseEntity.ok(shoppingListItemResponse)
    }
}