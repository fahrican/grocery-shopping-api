package com.udemy.groceryshoppingapi.user.web.rest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.udemy.groceryshoppingapi.auth.service.ClientSessionService
import com.udemy.groceryshoppingapi.dto.UserInfoResponse
import com.udemy.groceryshoppingapi.dto.UserInfoUpdateRequest
import com.udemy.groceryshoppingapi.dto.UserPasswordUpdateRequest
import com.udemy.groceryshoppingapi.user.service.AppUserService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testUser", roles = ["USER", "ADMIN"])
internal class AppUserControllerIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var mockUserProvider: ClientSessionService

    @MockBean
    private lateinit var mockService: AppUserService

    private val mapper = jacksonObjectMapper()

    private val userResponse =
        UserInfoResponse(firstName = "Omar", lastName = "Ramadan", email = "omar@aon.at", username = "omar030")


    @Test
    fun `when user info is requested then return success response`() {
        Mockito.`when`(mockService.fetchInfo()).thenReturn(userResponse)

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
        )
        resultActions.andExpect((MockMvcResultMatchers.status().isOk))
    }

    @Test
    fun `when change user email is triggered then return success response`() {
        val email = "hello@aon.at"
        val request = HashMap<String, String>()
        request["email"] = email
        Mockito.`when`(mockService.changeEmail(request)).thenReturn(userResponse)

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/user/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        )
        resultActions.andExpect((MockMvcResultMatchers.status().isOk))
    }

    @Test
    fun `when change username is triggered then return success response`() {
        val email = "ali-aziz"
        val request = HashMap<String, String>()
        request["username"] = email
        Mockito.`when`(mockService.changeEmail(request)).thenReturn(userResponse)

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/user/username")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        )
        resultActions.andExpect((MockMvcResultMatchers.status().isOk))
    }

    @Test
    fun `when change user password is triggered then return success response`() {
        val passwordRequest = UserPasswordUpdateRequest("oldPassword", "newPassword", "newPassword")
        Mockito.doNothing().`when`(mockService).changePassword(passwordRequest)

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/user/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(passwordRequest))
        )
        resultActions.andExpect((MockMvcResultMatchers.status().isOk))
    }

    @Test
    fun `when change user info is triggered then return success response`() {
        val updateRequest = UserInfoUpdateRequest(firstName = "Omar", lastName = "Ramadan")
        Mockito.`when`(mockService.changeInfo(updateRequest)).thenReturn(userResponse)

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/user/info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateRequest))
        )
        resultActions.andExpect((MockMvcResultMatchers.status().isOk))
    }
}