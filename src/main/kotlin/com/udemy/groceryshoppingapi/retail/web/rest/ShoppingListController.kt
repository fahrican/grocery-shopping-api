package com.udemy.groceryshoppingapi.retail.web.rest

import com.udemy.groceryshoppingapi.api.ShoppingListResource
import com.udemy.groceryshoppingapi.auth.service.ClientSessionService
import com.udemy.groceryshoppingapi.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.dto.ShoppingListUpdateRequest
import com.udemy.groceryshoppingapi.retail.service.ShoppingListService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ShoppingListController(
    private val service: ShoppingListService,
    private val userProvider: ClientSessionService
) : ShoppingListResource {
    override fun createShoppingList(shoppingListCreateRequest: ShoppingListCreateRequest): ResponseEntity<ShoppingListResponse> {
        val shoppingList = service.createShoppingList(shoppingListCreateRequest, userProvider.getAuthenticatedUser())
        return ResponseEntity(shoppingList, HttpStatus.CREATED)
    }

    override fun deleteShoppingList(id: Long): ResponseEntity<Unit> {
        service.deleteShoppingList(id, userProvider.getAuthenticatedUser())
        return ResponseEntity(Unit, HttpStatus.NO_CONTENT)
    }

    override fun getShoppingListById(id: Long): ResponseEntity<ShoppingListResponse> =
        ResponseEntity.ok(service.getShoppingListById(id, userProvider.getAuthenticatedUser()))


    override fun getShoppingLists(isDone: Boolean?): ResponseEntity<List<ShoppingListResponse>> {
        val shoppingLists = service.getShoppingLists(userProvider.getAuthenticatedUser(), isDone).toList()
        return ResponseEntity.ok(shoppingLists)
    }

    override fun updateShoppingList(
        id: Long,
        shoppingListUpdateRequest: ShoppingListUpdateRequest
    ): ResponseEntity<ShoppingListResponse> {
        val shoppingList =
            service.updateShoppingList(id, shoppingListUpdateRequest, userProvider.getAuthenticatedUser())
        return ResponseEntity.ok(shoppingList)
    }
}