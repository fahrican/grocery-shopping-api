package com.udemy.groceryshoppingapi.retail.repository

import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.user.entity.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ShoppingListRepository : JpaRepository<ShoppingList, Long> {

    @Query("SELECT s FROM ShoppingList s WHERE s.appUser = ?1")
    fun findAllByUser(appUser: AppUser): List<ShoppingList>?

    @Query("SELECT s FROM ShoppingList s WHERE s.id = ?1 AND s.appUser = ?2")
    fun findByIdAndUser(id: Long, appUser: AppUser): ShoppingList?

    @Query("SELECT s FROM ShoppingList s WHERE s.appUser = ?1 AND s.isDone = ?2")
    fun findAllByUserAndIsDone(appUser: AppUser, isDone: Boolean): List<ShoppingList>?
}