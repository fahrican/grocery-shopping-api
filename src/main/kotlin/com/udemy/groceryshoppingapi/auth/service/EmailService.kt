package com.udemy.groceryshoppingapi.auth.service

import com.udemy.groceryshoppingapi.user.entity.AppUser


interface EmailService {

    fun sendVerificationEmail(appUser: AppUser, token: String)

    fun sendPasswordResetEmail(appUser: AppUser, newPassword: String)
}