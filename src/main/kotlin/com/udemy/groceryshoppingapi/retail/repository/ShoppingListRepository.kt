package com.udemy.groceryshoppingapi.retail.repository

import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.user.entity.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ShoppingListRepository : JpaRepository<ShoppingList, Long> {

    fun findAllByAppUser(appUser: AppUser): List<ShoppingList>?

    fun findByIdAndAppUser(id: Long, appUser: AppUser): ShoppingList?

    fun findAllByAppUserAndDoneIs(appUser: AppUser, isDone: Boolean): List<ShoppingList>?
}