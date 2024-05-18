package com.udemy.groceryshoppingapi.retail.web.rest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.udemy.groceryshoppingapi.auth.service.ClientSessionService
import com.udemy.groceryshoppingapi.dto.Category
import com.udemy.groceryshoppingapi.dto.GroceryItemResponse
import com.udemy.groceryshoppingapi.dto.ShoppingListItemResponse
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
internal class ShoppingListItemsControllerIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var mockUserProvider: ClientSessionService

    @MockBean
    private lateinit var mockService: ShoppingListService

    private val mapper = jacksonObjectMapper()
    val response = ShoppingListItemResponse(
        id = 1,
        groceryItem = GroceryItemResponse(id = 1, name = "Grapefruit", category = Category.FRUITS),
        price = 11.20f,
        quantity = 1
    )
    private val listId = 1L

    @Test
    fun `when get shopping list items is called then return 200`() {
        Mockito.`when`(
            mockService.getShoppingListItems(listId, mockUserProvider.getAuthenticatedUser())
        ).thenReturn(setOf(response))

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/shopping-lists/$listId/shopping-list-items")
                .contentType(MediaType.APPLICATION_JSON)
        )

        resultActions.andExpect((status().isOk))
        resultActions.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(response.id))
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(response.price))
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity").value(response.quantity))
    }
}