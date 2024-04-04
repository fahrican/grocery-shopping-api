package com.udemy.groceryshoppingapi.user.repository

import com.udemy.groceryshoppingapi.user.entity.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AppUserRepository: JpaRepository<AppUser, Long> {

    fun findByEmail(email: String): AppUser?

    fun findByAppUsername(username: String): AppUser?
}