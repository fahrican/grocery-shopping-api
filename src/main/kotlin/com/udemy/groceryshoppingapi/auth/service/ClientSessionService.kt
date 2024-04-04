package com.udemy.groceryshoppingapi.auth.service

import com.udemy.groceryshoppingapi.user.entity.AppUser
import org.springframework.security.core.Authentication

interface ClientSessionService {

    fun retrieveAuthentication(): Authentication?

    fun findCurrentSessionUser(): AppUser

    fun getAuthenticatedUser(): AppUser
}