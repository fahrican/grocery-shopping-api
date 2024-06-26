package com.udemy.groceryshoppingapi.user.repository

import com.udemy.groceryshoppingapi.user.entity.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AppUserRepository : JpaRepository<AppUser, Long> {

    @Query("select u from AppUser u where u.email = ?1")
    fun findByEmail(email: String): AppUser?

    @Query("select u from AppUser u where u.appUsername = ?1")
    fun findByAppUsername(username: String): AppUser?
}