package com.udemy.groceryshoppingapi.user.util


import com.udemy.groceryshoppingapi.dto.UserInfoResponse
import com.udemy.groceryshoppingapi.user.entity.AppUser
import org.springframework.stereotype.Component

@Component
class UserInfoMapper {

    fun toDto(entity: AppUser) = UserInfoResponse(
        firstName = entity.firstName,
        lastName = entity.lastName,
        email = entity.email,
        username = entity.username
    )
}