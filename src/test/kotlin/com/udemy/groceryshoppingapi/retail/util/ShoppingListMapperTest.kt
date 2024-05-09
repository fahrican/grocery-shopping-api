package com.udemy.groceryshoppingapi.retail.util

import com.udemy.groceryshoppingapi.dto.Hypermarket
import com.udemy.groceryshoppingapi.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemResponse
import com.udemy.groceryshoppingapi.dto.SupermarketCreateRequest
import com.udemy.groceryshoppingapi.dto.SupermarketResponse
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import com.udemy.groceryshoppingapi.retail.entity.Supermarket
import com.udemy.groceryshoppingapi.user.entity.AppUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ShoppingListMapperTest {

    private val objectUnderTest = ShoppingListMapper()

    @Test
    fun `when to dto is called then expect the fields match`() {
        val shoppingList = ShoppingList(
            id = 1L,
            receiptPictureUrl = "http://example.com/receipt.jpg",
            isDone = true,
            supermarket = Supermarket(name = Hypermarket.BILLA),
            shoppingListItems = listOf(),
            appUser = AppUser()
        )
        val supermarketResponse = SupermarketResponse(id = 1, name = Hypermarket.BILLA)
        val shoppingListItemsResponse = listOf<ShoppingListItemResponse>()

        val dto = objectUnderTest.toDto(shoppingList, supermarketResponse, shoppingListItemsResponse)

        assertEquals(shoppingList.id, dto.id)
        assertEquals(shoppingList.receiptPictureUrl, dto.receiptPictureUrl)
        assertEquals(shoppingList.isDone, dto.isDone)
        assertEquals(supermarketResponse, dto.supermarket)
        assertEquals(shoppingListItemsResponse, dto.shoppingListItems)
        assertEquals(shoppingList.getTotalAmount(), dto.totalAmount)
    }

    @Test
    fun `when to entity is called then expect the fields match`() {
        val request = ShoppingListCreateRequest(
            receiptPictureUrl = "http://example.com/new_receipt.jpg",
            supermarket = SupermarketCreateRequest(name = Hypermarket.SPAR),
            shoppingListItems = listOf()
        )
        val supermarket = Supermarket(name = Hypermarket.SPAR)
        val shoppingListItems = listOf<ShoppingListItem>()
        val appUser = AppUser()

        val entity = objectUnderTest.toEntity(request, supermarket, shoppingListItems, appUser)

        assertEquals(request.receiptPictureUrl, entity.receiptPictureUrl)
        assertEquals(supermarket, entity.supermarket)
        assertEquals(shoppingListItems, entity.shoppingListItems)
        assertEquals(appUser, entity.appUser)
    }
}
