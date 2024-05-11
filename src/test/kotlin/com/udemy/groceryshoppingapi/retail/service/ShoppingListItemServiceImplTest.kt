package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.Category
import com.udemy.groceryshoppingapi.dto.GroceryItemCreateRequest
import com.udemy.groceryshoppingapi.dto.GroceryItemUpdateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemUpdateRequest
import com.udemy.groceryshoppingapi.retail.entity.GroceryItem
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import com.udemy.groceryshoppingapi.retail.repository.ShoppingListItemRepository
import com.udemy.groceryshoppingapi.retail.util.GroceryItemMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Optional

class ShoppingListItemServiceImplTest {

    private val mockRepository = mockk<ShoppingListItemRepository>(relaxed = true)

    private val mockGroceryItemService = mockk<GroceryItemService>(relaxed = true)

    private val groceryItemMapper = GroceryItemMapper()

    private val itemId = 1L

    private val shoppingListItem = ShoppingListItem(id = itemId, quantity = 2, price = 5.80F)

    private val objectUnderTest = ShoppingListItemServiceImpl(mockRepository, groceryItemMapper, mockGroceryItemService)

    @Test
    fun `when get shopping list item is called then expect the fields match`() {
        every { mockRepository.findById(itemId) } returns Optional.of(shoppingListItem)

        val actualResult = objectUnderTest.getShoppingListItem(itemId)

        assertEquals(shoppingListItem, actualResult)
    }

    @Test
    fun `when delete shopping list items is called then expect grocery items got deleted as well`() {
        val groceryItem = GroceryItem(id = 1, name = "Apple", category = Category.FRUITS)
        val listItems = listOf(
            ShoppingListItem(id = 2L, quantity = 1, price = 1.00F, groceryItem = groceryItem),
            shoppingListItem
        )
        mockRepository.saveAll(listItems)

        objectUnderTest.deleteShoppingListItems(listItems)

        assertEquals(0, mockRepository.findAll().size)
        verify(exactly = 1) { mockRepository.deleteAll(listItems) }
        verify(exactly = 1) { mockGroceryItemService.deleteGroceryItems(any()) }
    }

    @Test
    fun `when delete shopping list item is called then expect item got deleted`() {
        every { mockRepository.save(shoppingListItem) } returns shoppingListItem

        objectUnderTest.deleteShoppingListItem(shoppingListItem.id)

        assertEquals(0, mockRepository.findAll().size)
        verify(exactly = 1) { mockRepository.deleteShoppingListItem(shoppingListItem.id) }
    }

    @Test
    fun `when create shopping list item is called then check the properties of the persisted item`() {
        val shoppingList = ShoppingList()
        val createRequest = ShoppingListItemCreateRequest(
            quantity = 3,
            price = 10.50F,
            groceryItem = GroceryItemCreateRequest(name = "Milk", category = Category.DAIRY)
        )
        val groceryItem: GroceryItem = groceryItemMapper.toEntity(createRequest.groceryItem)
        val expectedShoppingListItem = ShoppingListItem(
            quantity = createRequest.quantity,
            price = createRequest.price,
            shoppingList = shoppingList,
            groceryItem = groceryItem
        )
        every { mockRepository.save(any()) } returns expectedShoppingListItem

        val actualResult: ShoppingListItem = objectUnderTest.createShoppingListItem(createRequest, shoppingList)

        verify {
            mockRepository.save(withArg {
                assertEquals(createRequest.quantity, it.quantity)
                assertEquals(createRequest.price, it.price)
                assertEquals(shoppingList, it.shoppingList)
            })
        }
        assertEquals(expectedShoppingListItem.groceryItem, actualResult.groceryItem)
    }

    @Test
    fun `when update shopping list is called then check the properties of the updated list`() {
        val shoppingList = ShoppingList(id = 1L)
        val shoppingListItems = listOf(
            shoppingListItem,
            ShoppingListItem(id = 2L, quantity = 3, price = 15.0F),
            ShoppingListItem(id = 3L, quantity = 2, price = 5.0F),
        )
        every { mockRepository.saveAllAndFlush(shoppingListItems) } returns shoppingListItems

        val actualResult: List<ShoppingListItem> = objectUnderTest.updateShoppingList(shoppingList, shoppingListItems)

        assertEquals(shoppingListItems, actualResult)
    }

    @Test
    fun `updateShoppingListItem updates and saves shopping list item`() {
        val id = 1L
        val groceryItemReq = GroceryItemUpdateRequest(name = "Milk", category = Category.DAIRY)
        val groceryItem = groceryItemMapper.toEntity(GroceryItemCreateRequest(name = "Milk", category = Category.DAIRY))
        val existingItem = ShoppingListItem(id, 5, 2.50F, null, groceryItem)
        val updateRequest = ShoppingListItemUpdateRequest(10, 5.00F, groceryItem = groceryItemReq)
        val updatedShoppingListItem = ShoppingListItem(
            id,
            quantity = updateRequest.quantity!!,
            price = updateRequest.price!!,
            shoppingList = null,
            groceryItem = groceryItem
        )
        every { mockRepository.findById(id) } returns Optional.of(existingItem)
        every { mockRepository.save(any()) } returns updatedShoppingListItem

        val actualResult: ShoppingListItem = objectUnderTest.updateShoppingListItem(id, updateRequest)

        verify {
            mockRepository.save(match {
                it.quantity == 10 && it.price == 5.00F
            })
        }
        assertEquals(updateRequest.quantity, actualResult.quantity)
        assertEquals(updateRequest.price, actualResult.price)
        assertEquals(updateRequest.groceryItem?.name, actualResult.groceryItem?.name)
        assertEquals(updateRequest.groceryItem?.category, actualResult.groceryItem?.category)
    }
}