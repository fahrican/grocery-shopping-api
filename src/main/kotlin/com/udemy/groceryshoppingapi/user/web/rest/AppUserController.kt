package com.udemy.groceryshoppingapi.user.web.rest

import com.udemy.groceryshoppingapi.api.UserResource
import com.udemy.groceryshoppingapi.dto.UserInfoResponse
import com.udemy.groceryshoppingapi.dto.UserInfoUpdateRequest
import com.udemy.groceryshoppingapi.dto.UserPasswordUpdateRequest
import com.udemy.groceryshoppingapi.user.service.AppUserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AppUserController(private val service: AppUserService) : UserResource {

    override fun changeEmail(
        requestBody: Map<String, String>
    ): ResponseEntity<UserInfoResponse> = ResponseEntity.ok(service.changeEmail(requestBody))

    override fun changePassword(
        userPasswordUpdateRequest: UserPasswordUpdateRequest
    ) = ResponseEntity.ok(service.changePassword(userPasswordUpdateRequest))

    override fun changeUsername(
        requestBody: Map<String, String>
    ): ResponseEntity<UserInfoResponse> = ResponseEntity.ok(service.changeUsername(requestBody))

    override fun fetchInfo(): ResponseEntity<UserInfoResponse> = ResponseEntity.ok(service.fetchInfo())

    override fun updateInfo(
        userInfoUpdateRequest: UserInfoUpdateRequest
    ): ResponseEntity<UserInfoResponse> = ResponseEntity.ok(service.changeInfo(userInfoUpdateRequest))
}
