package com.udemy.groceryshoppingapi.auth.util

import com.udemy.groceryshoppingapi.dto.RegisterRequest
import com.udemy.groceryshoppingapi.user.entity.AppUser
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class SignUpMapper {

    fun toEntity(request: RegisterRequest, passwordEncoder: PasswordEncoder) = AppUser().apply {
        firstName = request.firstName
        lastName = request.lastName
        email = request.email
        appUsername = request.username
        appPassword = passwordEncoder.encode(request.password)
    }
}