package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.Category
import com.udemy.groceryshoppingapi.dto.GroceryItemCreateRequest
import com.udemy.groceryshoppingapi.dto.Hypermarket
import com.udemy.groceryshoppingapi.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.dto.SupermarketCreateRequest
import com.udemy.groceryshoppingapi.error.BadRequestException
import com.udemy.groceryshoppingapi.error.ShoppingListNotFoundException
import com.udemy.groceryshoppingapi.error.SupermarketException
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import com.udemy.groceryshoppingapi.retail.entity.Supermarket
import com.udemy.groceryshoppingapi.retail.repository.ShoppingListRepository
import com.udemy.groceryshoppingapi.retail.util.ShoppingListItemMapper
import com.udemy.groceryshoppingapi.retail.util.ShoppingListMapper
import com.udemy.groceryshoppingapi.retail.util.SupermarketMapper
import com.udemy.groceryshoppingapi.user.entity.AppUser
import io.mockk.called
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

    private val appUser = AppUser()
    private val supermarket = Supermarket(name = Hypermarket.HOFER)
    private val shoppingListItem = ShoppingListItem(quantity = 5, price = 10.0F)
    private val id = 1L
    private val shoppingList = ShoppingList(
        id = id,
        receiptPictureUrl = null,
        isDone = false,
        appUser = appUser,
        supermarket = supermarket,
        shoppingListItems = mutableListOf(shoppingListItem)
    )

    @Test
    fun `when create shopping list is called then expect shopping list got created`() {
        // assign
        val shoppingListItemCreateRequest = ShoppingListItemCreateRequest(
            quantity = shoppingListItem.quantity,
            price = shoppingListItem.price,
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
        verify { mockShoppingListItemService.createShoppingListItem(any(), any()) wasNot called }
        verify { mockSupermarketService.findSupermarketByName(any()) wasNot called }
    }

    @Test
    fun `when shopping list by id is called then check for response properties`() {

        every { mockRepository.findByIdAndUser(any(), any()) } returns shoppingList

        val actualResult: ShoppingListResponse = objectUnderTest.getShoppingListById(id, appUser)

        assertEquals(shoppingList.shoppingListItems.size, actualResult.shoppingListItems?.size)
        assertEquals(shoppingList.supermarket?.name, actualResult.supermarket?.name)
        assertEquals(shoppingList.isDone, actualResult.isDone)
        assertEquals(shoppingList.receiptPictureUrl, actualResult.receiptPictureUrl)
        verify { mockRepository.findByIdAndUser(any(), any()) }
    }

    @Test
    fun `when shopping list by id is called then expect supermarket exception`() {
        val shoppingList = ShoppingList(
            id = id,
            receiptPictureUrl = null,
            isDone = false,
            appUser = appUser,
            supermarket = null,
            shoppingListItems = mutableListOf(shoppingListItem)
        )
        every { mockRepository.findByIdAndUser(any(), any()) } returns shoppingList

        val actualResult = assertThrows<SupermarketException> { objectUnderTest.getShoppingListById(id, appUser) }

        assertEquals("There is no supermarket associated with this shopping list!", actualResult.message)
    }

    @Test
    fun `when shopping list by id is called then expect shopping list not found exception`() {
        every { mockRepository.findByIdAndUser(any(), any()) } returns null

        val actualResult =
            assertThrows<ShoppingListNotFoundException> { objectUnderTest.getShoppingListById(id, appUser) }

        assertEquals("Shopping list with ID: $id does not exist!", actualResult.message)
    }

    @Test
    fun `when get shopping lists with is done to null is called then check for response properties`() {
        val shoppingLists = mutableListOf(shoppingList)
        every { mockRepository.findAllByUser(any()) } returns shoppingLists
        every { mockRepository.save(any()) } returns shoppingList

        val actualResult: Set<ShoppingListResponse> = objectUnderTest.getShoppingLists(appUser, null)

        assertEquals(shoppingLists.size, actualResult.size)
        assertEquals(shoppingLists.first().isDone, actualResult.first().isDone)
        assertEquals(shoppingLists.first().receiptPictureUrl, actualResult.first().receiptPictureUrl)
        assertEquals(shoppingLists.first().supermarket?.name, actualResult.first().supermarket?.name)
        assertEquals(shoppingLists.first().shoppingListItems.size, actualResult.first().shoppingListItems?.size)
        assertEquals(shoppingLists.first().getTotalAmount(), actualResult.first().totalAmount)
        verify { mockRepository.findAllByUser(any()) }
    }

    @Test
    fun `when get shopping lists with is done to null is called then expect empty set`() {
        every { mockRepository.findAllByUser(any()) } returns null
        every { mockRepository.save(any()) } returns shoppingList

        val actualResult: Set<ShoppingListResponse> = objectUnderTest.getShoppingLists(appUser, null)

        assertEquals(emptySet<ShoppingList>(), actualResult)
        verify { mockRepository.findAllByUser(any()) }
    }

    @Test
    fun `when get shopping lists with is done to false is called then check for response properties`() {
        val shoppingLists = mutableListOf(shoppingList)
        every { mockRepository.findAllByUserAndIsDone(any(), any()) } returns shoppingLists
        every { mockRepository.save(any()) } returns shoppingList

        val actualResult: Set<ShoppingListResponse> = objectUnderTest.getShoppingLists(appUser, false)

        assertEquals(shoppingLists.size, actualResult.size)
        assertEquals(shoppingLists.first().isDone, actualResult.first().isDone)
        assertEquals(shoppingLists.first().receiptPictureUrl, actualResult.first().receiptPictureUrl)
        assertEquals(shoppingLists.first().supermarket?.name, actualResult.first().supermarket?.name)
        assertEquals(shoppingLists.first().shoppingListItems.size, actualResult.first().shoppingListItems?.size)
        assertEquals(shoppingLists.first().getTotalAmount(), actualResult.first().totalAmount)
        verify { mockRepository.findAllByUserAndIsDone(any(), any()) }
    }
}