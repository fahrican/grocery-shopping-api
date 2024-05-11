package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.Category
import com.udemy.groceryshoppingapi.dto.GroceryItemCreateRequest
import com.udemy.groceryshoppingapi.dto.GroceryItemResponse
import com.udemy.groceryshoppingapi.dto.GroceryItemUpdateRequest
import com.udemy.groceryshoppingapi.retail.entity.GroceryItem
import com.udemy.groceryshoppingapi.retail.repository.GroceryItemRepository
import com.udemy.groceryshoppingapi.retail.util.GroceryItemMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Optional

class GroceryItemServiceImplTest {

    private val mockRepository = mockk<GroceryItemRepository>(relaxed = true)

    private val groceryItem = GroceryItem(id = 1L, name = "Apple", category = Category.FRUITS)
    private val mapper = GroceryItemMapper()
    private val objectUnderTest = GroceryItemServiceImpl(mockRepository, mapper)

    @Test
    fun `when delete grocery items is called then check for amount of items in the repository`() {
        val groceryItems = listOf(
            groceryItem,
            GroceryItem(name = "Banana", category = Category.FRUITS)
        )
        mockRepository.saveAll(groceryItems)

        objectUnderTest.deleteGroceryItems(groceryItems)

        assertEquals(0, mockRepository.findAll().size)
        verify(exactly = 1) { mockRepository.deleteAll(groceryItems) }
    }

    @Test
    fun `when delete grocery item is called then check if it got deleted`() {
        every { mockRepository.save(any()) } returns groceryItem

        objectUnderTest.deleteGroceryItem(groceryItem.id)

        assertEquals(0, mockRepository.findAll().size)
        verify(exactly = 1) { mockRepository.deleteGroceryItem(groceryItem.id) }
    }

    @Test
    fun `when create grocery item is called then check if item got persisted`() {
        val createReq = GroceryItemCreateRequest(
            name = "Orange",
            category = Category.FRUITS
        )
        val savedItem = GroceryItem(
            id = 1L,
            name = createReq.name,
            category = createReq.category
        )
        every { mockRepository.save(any()) } returns savedItem
        val expectedDto: GroceryItemResponse = mapper.toDto(savedItem)

        val actualResult: GroceryItemResponse = objectUnderTest.createGroceryItem(createReq)

        verify(exactly = 1) {
            mockRepository.save(withArg {
                assertEquals("Orange", it.name)
                assertEquals(Category.FRUITS, it.category)
            })
        }
        assertEquals(expectedDto, actualResult)
    }

    @Test
    fun `when update grocery item is called then check if item properties got updated`() {
        val grocerId = 1L
        val updateRequest = GroceryItemUpdateRequest(
            name = "Vimto",
            category = Category.BEVERAGES
        )
        val updatedGroceryItem = GroceryItem(
            id = grocerId,
            name = updateRequest.name!!,
            category = updateRequest.category!!
        )
        every { mockRepository.findById(any()) } returns Optional.of(groceryItem)
        every { mockRepository.save(any()) } returns updatedGroceryItem
        val expectedDto: GroceryItemResponse = mapper.toDto(updatedGroceryItem)

        val actualResult = objectUnderTest.updateGroceryItem(grocerId, updateRequest)

        verify(exactly = 1) { mockRepository.findById(grocerId) }
        verify(exactly = 1) {
            mockRepository.save(withArg {
                assertEquals("Vimto", it.name)
                assertEquals(Category.BEVERAGES, it.category)
            })
        }
        assertEquals(expectedDto, actualResult)
    }
}