package com.udemy.groceryshoppingapi.retail.web.rest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.udemy.groceryshoppingapi.auth.service.ClientSessionService
import com.udemy.groceryshoppingapi.dto.Hypermarket
import com.udemy.groceryshoppingapi.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.dto.ShoppingListUpdateRequest
import com.udemy.groceryshoppingapi.dto.SupermarketCreateRequest
import com.udemy.groceryshoppingapi.dto.SupermarketResponse
import com.udemy.groceryshoppingapi.dto.SupermarketUpdateRequest
import com.udemy.groceryshoppingapi.retail.service.ShoppingListService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ShoppingListControllerIT {


    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var mockUserProvider: ClientSessionService

    @MockBean
    private lateinit var mockService: ShoppingListService

    private val mapper = jacksonObjectMapper()
    private val response = ShoppingListResponse(
        id = 1,
        isDone = false,
        receiptPictureUrl = "https://example.com/receipt.jpg",
        supermarket = SupermarketResponse(market = Hypermarket.ETSAN),
        shoppingListItems = emptyList(),
        totalAmount = 0.00F
    )
    private val listId: Long = 99

    @Test
    fun `when create shopping list then expect 201 status`() {
        val createReq = ShoppingListCreateRequest(
            receiptPictureUrl = "https://xyz.com/list.jpg",
            supermarket = SupermarketCreateRequest(Hypermarket.HOFER),
            shoppingListItems = emptyList()
        )
        Mockito.`when`(
            mockService.createShoppingList(createReq, mockUserProvider.getAuthenticatedUser())
        ).thenReturn(response)

        val result: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/shopping-lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createReq))
        )

        result.andExpect((status().isCreated))
        result.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(response.id))
        result.andExpect(MockMvcResultMatchers.jsonPath("$.isDone").value(response.isDone))
        result.andExpect(MockMvcResultMatchers.jsonPath("$.receiptPictureUrl").value(response.receiptPictureUrl))
        result.andExpect(MockMvcResultMatchers.jsonPath("$.totalAmount").value(response.totalAmount))
    }

    @Test
    fun `when delete shopping list is called then expect 204 status`() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/v1/shopping-lists/$listId")
                .contentType(MediaType.APPLICATION_JSON)
        )

        resultActions.andExpect((status().isNoContent))
    }

    @Test
    fun `when get shopping list is called then expect 200 status`() {
        Mockito.`when`(
            mockService.getShoppingListById(listId, mockUserProvider.getAuthenticatedUser())
        ).thenReturn(response)

        val result: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/shopping-lists/$listId")
                .contentType(MediaType.APPLICATION_JSON)
        )

        result.andExpect((status().isOk))
        result.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(response.id))
        result.andExpect(MockMvcResultMatchers.jsonPath("$.isDone").value(response.isDone))
        result.andExpect(MockMvcResultMatchers.jsonPath("$.receiptPictureUrl").value(response.receiptPictureUrl))
        result.andExpect(MockMvcResultMatchers.jsonPath("$.totalAmount").value(response.totalAmount))
    }

    @Test
    fun `when get shopping lists is called then expect 200 status`() {
        Mockito.`when`(
            mockService.getShoppingLists(mockUserProvider.getAuthenticatedUser(), null)
        ).thenReturn(setOf(response))

        val result: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/shopping-lists")
                .contentType(MediaType.APPLICATION_JSON)
        )

        result.andExpect((status().isOk))
        result.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        result.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(response.id))
        result.andExpect(MockMvcResultMatchers.jsonPath("$[0].isDone").value(response.isDone))
        result.andExpect(MockMvcResultMatchers.jsonPath("$[0].receiptPictureUrl").value(response.receiptPictureUrl))
        result.andExpect(MockMvcResultMatchers.jsonPath("$[0].totalAmount").value(response.totalAmount))
    }

    @Test
    fun `when update shopping list is called then expect 200 status`() {
        val updateReq = ShoppingListUpdateRequest(
            receiptPictureUrl = "https://xyz.com/list.jpg",
            supermarket = SupermarketUpdateRequest(Hypermarket.HOFER),
            isDone = false
        )
        Mockito.`when`(
            mockService.updateShoppingList(listId, updateReq, mockUserProvider.getAuthenticatedUser())
        ).thenReturn(response)

        val result: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/shopping-lists/$listId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateReq))
        )

        result.andExpect((status().isOk))
        result.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(response.id))
        result.andExpect(MockMvcResultMatchers.jsonPath("$.isDone").value(response.isDone))
        result.andExpect(MockMvcResultMatchers.jsonPath("$.receiptPictureUrl").value(response.receiptPictureUrl))
        result.andExpect(MockMvcResultMatchers.jsonPath("$.totalAmount").value(response.totalAmount))
    }
}