package com.udemy.groceryshoppingapi.retail.web.rest

import com.udemy.groceryshoppingapi.api.GroceryItemResource
import com.udemy.groceryshoppingapi.auth.service.ClientSessionService
import com.udemy.groceryshoppingapi.dto.GroceryItemResponse
import com.udemy.groceryshoppingapi.dto.GroceryItemUpdateRequest
import com.udemy.groceryshoppingapi.retail.service.ShoppingListService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class GroceryItemController(
    private val userProvider: ClientSessionService,
    private val shoppingListService: ShoppingListService
) : GroceryItemResource {

    override fun getGroceryItem(listId: Long, itemId: Long) =
        ResponseEntity.ok(
            shoppingListService.getGroceryItem(
                listId,
                itemId,
                userProvider.getAuthenticatedUser()
            )
        )

    override fun updateGroceryItem(
        listId: Long,
        itemId: Long,
        groceryId: Long,
        groceryItemUpdateRequest: GroceryItemUpdateRequest
    ): ResponseEntity<GroceryItemResponse> {
        val grocerItem = shoppingListService.updateGroceryItem(groceryId, groceryItemUpdateRequest)
        return ResponseEntity.ok(grocerItem)
    }
}
