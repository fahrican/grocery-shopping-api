package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.Category
import com.udemy.groceryshoppingapi.dto.GroceryItemCreateRequest
import com.udemy.groceryshoppingapi.dto.Hypermarket
import com.udemy.groceryshoppingapi.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.dto.SupermarketCreateRequest
import com.udemy.groceryshoppingapi.error.BadRequestException
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import com.udemy.groceryshoppingapi.retail.entity.Supermarket
import com.udemy.groceryshoppingapi.retail.repository.ShoppingListRepository
import com.udemy.groceryshoppingapi.retail.util.ShoppingListItemMapper
import com.udemy.groceryshoppingapi.retail.util.ShoppingListMapper
import com.udemy.groceryshoppingapi.retail.util.SupermarketMapper
import com.udemy.groceryshoppingapi.user.entity.AppUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ShoppingListServiceImplTest {

    private val shoppingListMapper = ShoppingListMapper()

    private val supermarketMapper = SupermarketMapper()

    private val mockShoppingListItemMapper = mockk<ShoppingListItemMapper>(relaxed = true)

    private val mockRepository = mockk<ShoppingListRepository>(relaxed = true)

    private val mockSupermarketService = mockk<SupermarketService>(relaxed = true)

    private val mockGroceryItemService = mockk<GroceryItemService>(relaxed = true)

    private val mockShoppingListItemService = mockk<ShoppingListItemService>(relaxed = true)

    private val objectUnderTest = ShoppingListServiceImpl(
        shoppingListMapper,
        mockRepository,
        supermarketMapper,
        mockSupermarketService,
        mockShoppingListItemMapper,
        mockShoppingListItemService,
        mockGroceryItemService
    )

    @Test
    fun `when create shopping list is called then expect shopping list got created`() {
        // assign
        val appUser = AppUser()
        val supermarket = Supermarket(name = Hypermarket.HOFER)
        val shoppingListItem = ShoppingListItem(quantity = 5, price = 10.0F)
        val shoppingListItemCreateRequest = ShoppingListItemCreateRequest(
            quantity = 5,
            price = 10.0F,
            groceryItem = GroceryItemCreateRequest(name = "Milk", category = Category.DAIRY)
        )
        val createRequest = ShoppingListCreateRequest(
            supermarket = SupermarketCreateRequest(Hypermarket.HOFER),
            shoppingListItems = mutableListOf(shoppingListItemCreateRequest)
        )
        val shoppingListItems: List<ShoppingListItem> = mutableListOf(shoppingListItem)
        every { mockShoppingListItemService.createShoppingListItem(any(), any()) } returns shoppingListItem
        every { mockSupermarketService.findSupermarketByName(any()) } returns supermarket
        val shoppingList: ShoppingList =
            shoppingListMapper.toEntity(createRequest, supermarket, shoppingListItems, appUser)
        every { mockRepository.save(any()) } returns shoppingList
        every {
            mockShoppingListItemService.updateShoppingList(
                shoppingList,
                shoppingListItems
            )
        } returns shoppingListItems

        // act
        val actualResult: ShoppingListResponse = objectUnderTest.createShoppingList(createRequest, appUser)

        // assert
        assertEquals(shoppingListItems.size, actualResult.shoppingListItems?.size)
        assertEquals(supermarket.name, actualResult.supermarket?.name)
        assertEquals(false, actualResult.isDone)
        assertEquals(10.0f, actualResult.totalAmount)
        assertEquals(null, actualResult.receiptPictureUrl)
        verify { mockShoppingListItemService.createShoppingListItem(any(), any()) }
        verify { mockSupermarketService.findSupermarketByName(any()) }
        verify { mockShoppingListItemService.updateShoppingList(any(), any()) }
    }

    @Test
    fun `when create shopping list is called then expect exception case`() {
        val appUser = AppUser()
        val createRequest = ShoppingListCreateRequest(
            supermarket = SupermarketCreateRequest(Hypermarket.HOFER),
            shoppingListItems = mutableListOf()
        )

        val actualResult =
            assertThrows<BadRequestException> { objectUnderTest.createShoppingList(createRequest, appUser) }

        assertEquals("A shopping list must have at least one item", actualResult.message)
    }

}