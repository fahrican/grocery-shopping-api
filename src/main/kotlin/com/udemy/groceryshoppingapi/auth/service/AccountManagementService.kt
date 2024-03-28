package com.udemy.groceryshoppingapi.auth.service

import com.udemy.groceryshoppingapi.models.AuthenticationRequest
import com.udemy.groceryshoppingapi.models.AuthenticationResponse
import com.udemy.groceryshoppingapi.models.EmailConfirmedResponse
import com.udemy.groceryshoppingapi.models.RegisterRequest


interface AccountManagementService {

    fun signUp(request: RegisterRequest): EmailConfirmedResponse

    fun verifyUser(token: String): EmailConfirmedResponse

    fun signIn(request: AuthenticationRequest): AuthenticationResponse

    fun requestPasswordReset(email: String): EmailConfirmedResponse
}