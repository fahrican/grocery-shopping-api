package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.Category
import com.udemy.groceryshoppingapi.dto.GroceryItemCreateRequest
import com.udemy.groceryshoppingapi.dto.GroceryItemResponse
import com.udemy.groceryshoppingapi.dto.GroceryItemUpdateRequest
import com.udemy.groceryshoppingapi.dto.Hypermarket
import com.udemy.groceryshoppingapi.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemResponse
import com.udemy.groceryshoppingapi.dto.ShoppingListItemUpdateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.dto.ShoppingListUpdateRequest
import com.udemy.groceryshoppingapi.dto.SupermarketCreateRequest
import com.udemy.groceryshoppingapi.dto.SupermarketUpdateRequest
import com.udemy.groceryshoppingapi.error.BadRequestException
import com.udemy.groceryshoppingapi.error.ShoppingListItemNotFoundException
import com.udemy.groceryshoppingapi.error.ShoppingListNotFoundException
import com.udemy.groceryshoppingapi.error.SupermarketException
import com.udemy.groceryshoppingapi.retail.entity.GroceryItem
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import com.udemy.groceryshoppingapi.retail.entity.Supermarket
import com.udemy.groceryshoppingapi.retail.repository.ShoppingListItemRepository
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
        assertEquals(supermarket.name, actualResult.supermarket?.market)
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
        assertEquals(shoppingList.supermarket?.name, actualResult.supermarket?.market)
        assertEquals(shoppingList.isDone, actualResult.isDone)
        assertEquals(shoppingList.receiptPictureUrl, actualResult.receiptPictureUrl)
        verify { mockRepository.findByIdAndUser(any(), any()) }
    }

    @Test
    fun `when shopping list by id is called then expect supermarket exception`() {
        shoppingList.supermarket = null
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
        assertEquals(shoppingLists.first().supermarket?.name, actualResult.first().supermarket?.market)
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
        assertEquals(shoppingLists.first().supermarket?.name, actualResult.first().supermarket?.market)
        assertEquals(shoppingLists.first().shoppingListItems.size, actualResult.first().shoppingListItems?.size)
        assertEquals(shoppingLists.first().getTotalAmount(), actualResult.first().totalAmount)
        verify { mockRepository.findAllByUserAndIsDone(any(), any()) }
    }

    @Test
    fun `when update shopping list is called then check for the updated properties`() {
        val etsan = Supermarket(name = Hypermarket.ETSAN)
        val updateRequest = ShoppingListUpdateRequest(
            receiptPictureUrl = "https://example.com/new_receipt.jpg",
            isDone = true,
            supermarket = SupermarketUpdateRequest(etsan.name)
        )
        val expectedShoppingList = ShoppingList(
            id = id,
            appUser = appUser,
            receiptPictureUrl = updateRequest.receiptPictureUrl,
            isDone = updateRequest.isDone!!,
            supermarket = etsan
        )
        every { mockSupermarketService.findSupermarketByName(any()) } returns etsan
        every { mockRepository.save(any()) } returns expectedShoppingList

        val actualResult: ShoppingListResponse = objectUnderTest.updateShoppingList(id, updateRequest, appUser)

        assertEquals(updateRequest.receiptPictureUrl, actualResult.receiptPictureUrl)
        assertEquals(updateRequest.isDone, actualResult.isDone)
        assertEquals(updateRequest.supermarket?.market?.value, actualResult.supermarket?.market?.value)
        verify { mockSupermarketService.findSupermarketByName(any()) }
        verify { mockRepository.save(any()) }
    }

    @Test
    fun `when update shopping list is called with supermarket null then check for the updated properties`() {
        val updateRequest = ShoppingListUpdateRequest(
            receiptPictureUrl = "https://example.com/new_receipt.jpg",
            isDone = true,
            supermarket = null
        )
        val expectedShoppingList = ShoppingList(
            id = id,
            appUser = appUser,
            receiptPictureUrl = updateRequest.receiptPictureUrl,
            isDone = updateRequest.isDone!!,
            supermarket = null
        )
        every { mockRepository.save(any()) } returns expectedShoppingList

        val actualResult: ShoppingListResponse = objectUnderTest.updateShoppingList(id, updateRequest, appUser)

        assertEquals(updateRequest.receiptPictureUrl, actualResult.receiptPictureUrl)
        assertEquals(updateRequest.isDone, actualResult.isDone)
        verify { mockSupermarketService.findSupermarketByName(any()) wasNot called }
        verify { mockRepository.save(any()) }
    }

    @Test
    fun `when delete shopping list is called with one saved shopping list then expect repository to be empty`() {
        val shoppingLists = mutableListOf(shoppingList)
        mockRepository.saveAll(shoppingLists)
        every { mockRepository.findByIdAndUser(any(), any()) } returns shoppingList
        every { mockShoppingListItemService.deleteShoppingListItems(any()) } returns Unit

        objectUnderTest.deleteShoppingList(id, appUser)

        assertEquals(0, mockRepository.findAll().size)
        verify { mockRepository.findByIdAndUser(any(), any()) }
        verify { mockShoppingListItemService.deleteShoppingListItems(any()) }
    }

    @Test
    fun `when update grocery item called then check for response`() {
        val groceryItemResponse = GroceryItemResponse(id, "Apple", Category.FRUITS)
        every { mockGroceryItemService.updateGroceryItem(any(), any()) } returns groceryItemResponse

        val actualResult: GroceryItemResponse =
            objectUnderTest.updateGroceryItem(id, GroceryItemUpdateRequest("Apple", Category.FRUITS))

        assertEquals(groceryItemResponse, actualResult)
        verify { mockGroceryItemService.updateGroceryItem(any(), any()) }
    }

    @Test
    fun `when get shopping list items is called then check for the result`() {
        // assign
        val shoppingLists: List<ShoppingList> = mutableListOf(shoppingList)
        mockRepository.saveAll(shoppingLists)
        every { mockRepository.findByIdAndUser(any(), any()) } returns shoppingList
        val listItemResponse: ShoppingListItemResponse = mockShoppingListItemMapper.toDto(shoppingListItem)
        val expectedResponse: Set<ShoppingListItemResponse> = setOf(listItemResponse)

        // act
        val actualResult: Set<ShoppingListItemResponse> = objectUnderTest.getShoppingListItems(id, appUser)

        // assert
        assertEquals(expectedResponse.size, actualResult.size)
        assertEquals(expectedResponse, actualResult)
        verify { mockRepository.findByIdAndUser(any(), any()) }
    }

    @Test
    fun `when get grocery item is called then check for the result`() {
        val groceryItemResponse = GroceryItemResponse(id = 1, name = "Orange", category = Category.FRUITS)
        every { mockRepository.findByIdAndUser(shoppingList.id, appUser) } returns shoppingList
        val shoppingListItemResponse = ShoppingListItemResponse(
            id = shoppingListItem.id,
            groceryItem = groceryItemResponse,
            price = 11.20f,
            quantity = 1
        )
        every { mockShoppingListItemMapper.toDto(any()) } returns shoppingListItemResponse

        val actualResult: GroceryItemResponse =
            objectUnderTest.getGroceryItem(shoppingList.id, shoppingListItem.id, appUser)

        assertEquals(groceryItemResponse, actualResult)
        verify { mockRepository.findByIdAndUser(shoppingList.id, appUser) }
    }

    @Test
    fun `when get shopping list item is called then check for the result`() {
        val groceryItemResponse = GroceryItemResponse(id = 1, name = "Orange", category = Category.FRUITS)
        every { mockRepository.findByIdAndUser(shoppingList.id, appUser) } returns shoppingList
        val shoppingListItemResponse = ShoppingListItemResponse(
            id = shoppingListItem.id,
            groceryItem = groceryItemResponse,
            price = 11.20f,
            quantity = 1
        )
        every { mockShoppingListItemMapper.toDto(any()) } returns shoppingListItemResponse

        val actualResult: ShoppingListItemResponse =
            objectUnderTest.getShoppingListItem(shoppingList.id, shoppingListItem.id, appUser)

        assertEquals(shoppingListItemResponse, actualResult)
        verify { mockRepository.findByIdAndUser(shoppingList.id, appUser) }
    }

    @Test
    fun `when get shopping list item is called then expect exception`() {
        val shoppingList = ShoppingList(
            id = 1,
            receiptPictureUrl = "https://example.com/receipt.jpg",
            appUser = appUser,
            supermarket = supermarket,
            shoppingListItems = emptyList()
        )
        val expectedException = BadRequestException("Shopping list item with ID: 0 does not exist!")
        every { mockRepository.findByIdAndUser(any(), any()) } returns shoppingList

        val actualException: BadRequestException =
            assertThrows { objectUnderTest.getShoppingListItem(shoppingList.id, shoppingListItem.id, appUser) }

        assertEquals(expectedException.message, actualException.message)
        verify { mockRepository.findByIdAndUser(shoppingList.id, appUser) }
    }

    @Test
    fun `when create shopping list item is called then check for result`() {
        shoppingListItem.groceryItem = GroceryItem(name = "Hummus", category = Category.OTHER)
        val listItemCreateReq = ShoppingListItemCreateRequest(
            price = shoppingListItem.price,
            quantity = shoppingListItem.quantity,
            groceryItem = GroceryItemCreateRequest(
                name = shoppingListItem.groceryItem!!.name,
                category = shoppingListItem.groceryItem!!.category
            )
        )
        val expectedShoppingListItemResponse = ShoppingListItemResponse(
            id = shoppingListItem.id,
            quantity = shoppingListItem.quantity,
            price = shoppingListItem.price,
            groceryItem = GroceryItemResponse()
        )
        every { mockRepository.findByIdAndUser(any(), any()) } returns shoppingList
        every { mockShoppingListItemService.createShoppingListItem(any(), any()) } returns shoppingListItem
        every { mockShoppingListItemMapper.toDto(any()) } returns expectedShoppingListItemResponse

        val actualResult: ShoppingListItemResponse =
            objectUnderTest.createShoppingListItem(shoppingList.id, listItemCreateReq, appUser)

        assertEquals(expectedShoppingListItemResponse, actualResult)
        verify { mockRepository.findByIdAndUser(any(), any()) }
        verify { mockShoppingListItemService.createShoppingListItem(any(), any()) }
    }

    @Test
    fun `when update shopping list item is called then check for result`() {
        val listItemUpdateReq = ShoppingListItemUpdateRequest(
            price = 0.99F,
            quantity = 3
        )
        val expectedShoppingListItemResponse = ShoppingListItemResponse(
            id = shoppingListItem.id,
            quantity = listItemUpdateReq.quantity!!,
            price = listItemUpdateReq.price!!,
            groceryItem = GroceryItemResponse()
        )
        every { mockShoppingListItemService.updateShoppingListItem(any(), any()) } returns shoppingListItem
        every { mockShoppingListItemMapper.toDto(any()) } returns expectedShoppingListItemResponse

        val actualResult: ShoppingListItemResponse =
            objectUnderTest.updateShoppingListItem(shoppingList.id, shoppingListItem.id, listItemUpdateReq, appUser)

        assertEquals(expectedShoppingListItemResponse, actualResult)
        verify { mockShoppingListItemService.updateShoppingListItem(any(), any()) }
    }

    @Test
    fun `when delete shopping list item is called then check for result`() {
        val mockShoppingListItemRepository = mockk<ShoppingListItemRepository>(relaxed = true)
        every { mockRepository.findByIdAndUser(any(), any()) } returns shoppingList
        mockShoppingListItemRepository.saveAll(shoppingList.shoppingListItems)

        objectUnderTest.deleteShoppingListItem(shoppingList.id, 0, appUser)

        assertEquals(0, mockShoppingListItemRepository.findAll().size)
        verify { mockRepository.findByIdAndUser(any(), any()) }
        verify { mockShoppingListItemService.deleteShoppingListItem(any()) }
    }

    @Test
    fun `when delete shopping list item is called then expect shopping list item not found exception`() {
        val listId = 1L
        val itemId = 100L
        val shoppingList = ShoppingList(id = listId, shoppingListItems = mutableListOf())
        every { mockRepository.findByIdAndUser(any(), any()) } returns shoppingList

        val actualResult = assertThrows<ShoppingListItemNotFoundException> {
            objectUnderTest.deleteShoppingListItem(
                listId,
                itemId,
                appUser
            )
        }

        assertEquals("Shopping list item with ID: $itemId does not exist!", actualResult.message)
    }
}