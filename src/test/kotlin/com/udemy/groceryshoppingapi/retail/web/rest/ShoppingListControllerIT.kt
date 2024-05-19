package com.udemy.groceryshoppingapi.retail.web.rest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.udemy.groceryshoppingapi.auth.service.ClientSessionService
import com.udemy.groceryshoppingapi.dto.Hypermarket
import com.udemy.groceryshoppingapi.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.dto.SupermarketCreateRequest
import com.udemy.groceryshoppingapi.dto.SupermarketResponse
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

    @Test
    fun `when create shopping list then expect 200 status`() {
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
}