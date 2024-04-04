package com.udemy.groceryshoppingapi.auth.service

import org.springframework.security.core.userdetails.UserDetails

interface JwtService {

    fun extractUsername(token: String): String

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean

    fun generateAccessToken(userDetails: UserDetails): String

    fun generateRefreshToken(userDetails: UserDetails): String
}