package com.udemy.groceryshoppingapi.auth.web.rest

import com.udemy.groceryshoppingapi.api.AuthenticationResource
import com.udemy.groceryshoppingapi.auth.service.AccountManagementService
import com.udemy.groceryshoppingapi.dto.AuthenticationRequest
import com.udemy.groceryshoppingapi.dto.AuthenticationResponse
import com.udemy.groceryshoppingapi.dto.EmailConfirmedResponse
import com.udemy.groceryshoppingapi.dto.RegisterRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(
    private val service: AccountManagementService
) : AuthenticationResource {

    override fun requestPasswordReset(
        email: String
    ): ResponseEntity<EmailConfirmedResponse> = ResponseEntity.ok(service.requestPasswordReset(email))


    override fun signIn(
        authenticationRequest: AuthenticationRequest
    ): ResponseEntity<AuthenticationResponse> = ResponseEntity.ok(service.signIn(authenticationRequest))


    override fun signUp(registerRequest: RegisterRequest): ResponseEntity<EmailConfirmedResponse> {
        val response = service.signUp(registerRequest)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    override fun verifyUser(token: String): ResponseEntity<EmailConfirmedResponse> {
        return ResponseEntity.ok(service.verifyUser(token))
    }
}