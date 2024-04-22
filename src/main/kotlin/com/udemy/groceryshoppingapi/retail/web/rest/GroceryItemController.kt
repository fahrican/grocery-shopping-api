package com.udemy.groceryshoppingapi.retail.web.rest

import com.udemy.groceryshoppingapi.api.GroceryItemResource
import com.udemy.groceryshoppingapi.auth.service.ClientSessionService
import com.udemy.groceryshoppingapi.retail.service.ShoppingListService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class GroceryItemController(
    private val userProvider: ClientSessionService,
    private val shoppingListService: ShoppingListService
) : GroceryItemResource {

    override fun getGroceryItem(shoppingListId: Long, shoppingListItemId: Long) =
        ResponseEntity.ok(
            shoppingListService.getGroceryItem(
                shoppingListId,
                shoppingListItemId,
                userProvider.getAuthenticatedUser()
            )
        )
}
