package com.udemy.groceryshoppingapi.retail.util

import com.udemy.groceryshoppingapi.dto.Category
import com.udemy.groceryshoppingapi.dto.GroceryItemCreateRequest
import com.udemy.groceryshoppingapi.retail.entity.GroceryItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GroceryItemMapperTest {

    private val objectUnderTest = GroceryItemMapper()

    @Test
    fun `when to dto is called then expect the fields match`() {
        val groceryItem = GroceryItem(id = 1L, name = "Apples", category = Category.FRUITS)

        val actualDto = objectUnderTest.toDto(groceryItem)

        assertEquals(groceryItem.id, actualDto.id)
        assertEquals(groceryItem.name, actualDto.name)
        assertEquals(groceryItem.category, actualDto.category)
    }

    @Test
    fun `when to entity is called then expect the fields match`() {
        val groceryItemCreateRequest = GroceryItemCreateRequest(name = "Bananas", category = Category.FRUITS)

        val actualEntity = objectUnderTest.toEntity(groceryItemCreateRequest)

        assertEquals(groceryItemCreateRequest.name, actualEntity.name)
        assertEquals(groceryItemCreateRequest.category, actualEntity.category)
    }
}