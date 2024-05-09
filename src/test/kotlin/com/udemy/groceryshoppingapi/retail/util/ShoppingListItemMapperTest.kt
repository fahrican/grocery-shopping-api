package com.udemy.groceryshoppingapi.retail.util

import com.udemy.groceryshoppingapi.dto.Category
import com.udemy.groceryshoppingapi.dto.GroceryItemCreateRequest
import com.udemy.groceryshoppingapi.dto.GroceryItemResponse
import com.udemy.groceryshoppingapi.dto.ShoppingListItemCreateRequest
import com.udemy.groceryshoppingapi.retail.entity.GroceryItem
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ShoppingListItemMapperTest {

    private val groceryItemMapper = mockk<GroceryItemMapper>()
    private val objectUnderTest = ShoppingListItemMapper(groceryItemMapper)

    @Test
    fun `when to dto is called then expect the fields match`() {
        val groceryItem = GroceryItem(id = 1, name = "Apples", category = Category.FRUITS)
        val entity = ShoppingListItem(id = 1L, quantity = 5, price = 2.50F, groceryItem = groceryItem)
        val groceryItemResponse = GroceryItemResponse(1, "Apples", Category.FRUITS)
        every { groceryItemMapper.toDto(groceryItem) } returns groceryItemResponse

        val dto = objectUnderTest.toDto(entity)

        assertEquals(entity.id, dto.id)
        assertEquals(entity.quantity, dto.quantity)
        assertEquals(entity.price, dto.price)
        assertEquals(groceryItemResponse, dto.groceryItem)
    }

    @Test
    fun `when to entity is called then expect the fields match`() {
        val groceryItemRequest = GroceryItemCreateRequest(name = "Bananas", category = Category.FRUITS)
        val groceryItemMapper = GroceryItemMapper()
        val shoppingList = ShoppingList(id = 1)
        val request = ShoppingListItemCreateRequest(quantity = 3, price = 1.75F, groceryItem = groceryItemRequest)
        val groceryItem = groceryItemMapper.toEntity(groceryItemRequest)
        val entity = objectUnderTest.toEntity(request, shoppingList, groceryItem)

        assertEquals(request.quantity, entity.quantity)
        assertEquals(request.price, entity.price)
        assertEquals(shoppingList, entity.shoppingList)
        assertEquals(groceryItem, entity.groceryItem)
    }
}