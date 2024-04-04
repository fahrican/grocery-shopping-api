package com.udemy.groceryshoppingapi.user.service

import com.udemy.groceryshoppingapi.dto.UserInfoResponse
import com.udemy.groceryshoppingapi.dto.UserInfoUpdateRequest
import com.udemy.groceryshoppingapi.dto.UserPasswordUpdateRequest

interface AppUserService {

    fun changeEmail(request: Map<String, String>): UserInfoResponse

    fun changeUsername(request: Map<String, String>): UserInfoResponse

    fun changePassword(request: UserPasswordUpdateRequest)

    fun changeInfo(request: UserInfoUpdateRequest): UserInfoResponse

    fun fetchInfo(): UserInfoResponse
}