package com.udemy.groceryshoppingapi.auth.service

import com.udemy.groceryshoppingapi.dto.AuthenticationRequest
import com.udemy.groceryshoppingapi.dto.AuthenticationResponse
import com.udemy.groceryshoppingapi.dto.EmailConfirmedResponse
import com.udemy.groceryshoppingapi.dto.RegisterRequest


interface AccountManagementService {

    fun signUp(request: RegisterRequest): EmailConfirmedResponse

    fun verifyUser(token: String): EmailConfirmedResponse

    fun signIn(request: AuthenticationRequest): AuthenticationResponse

    fun requestPasswordReset(email: String): EmailConfirmedResponse
}