package com.udemy.groceryshoppingapi.user.util

import com.udemy.groceryshoppingapi.user.entity.AppUser
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class UserInfoMapperTest {

    @Test
    fun `when entity to dto is triggered then correct user info response`() {
        val appUser = mockk<AppUser>()
        every { appUser.firstName } returns "Maya"
        every { appUser.lastName } returns "Diab"
        every { appUser.email } returns "maya.diab@example.com"
        every { appUser.username } returns "maya-diab"
        val userInfoMapper = UserInfoMapper()

        val userInfoResponse = userInfoMapper.toDto(appUser)

        assertEquals("Maya", userInfoResponse.firstName)
        assertEquals("Diab", userInfoResponse.lastName)
        assertEquals("maya.diab@example.com", userInfoResponse.email)
        assertEquals("maya-diab", userInfoResponse.username)
    }
}