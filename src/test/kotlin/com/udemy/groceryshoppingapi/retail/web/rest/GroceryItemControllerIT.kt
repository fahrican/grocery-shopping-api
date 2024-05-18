package com.udemy.groceryshoppingapi.retail.web.rest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.udemy.groceryshoppingapi.auth.service.ClientSessionService
import com.udemy.groceryshoppingapi.dto.Category
import com.udemy.groceryshoppingapi.dto.GroceryItemResponse
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

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class GroceryItemControllerIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var mockUserProvider: ClientSessionService

    @MockBean
    private lateinit var mockShoppingListService: ShoppingListService

    private val mapper = jacksonObjectMapper()

    private val response = GroceryItemResponse(id = 1, name = "Apple", category = Category.FRUITS)
    private val listId = 1L
    private val itemId = 1L

    @Test
    fun `when get grocery item called then expect 200 status`() {
        Mockito.`when`(
            mockShoppingListService.getGroceryItem(
                listId,
                itemId,
                mockUserProvider.getAuthenticatedUser()
            )
        ).thenReturn(response)

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/shopping-lists/$listId/shopping-list-items/$itemId/grocery-item")
                .contentType(MediaType.APPLICATION_JSON)
        )

        resultActions.andExpect((MockMvcResultMatchers.status().isOk))
        resultActions.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(response.id))
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(response.name))
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.category").value(response.category.toString()))
    }

}