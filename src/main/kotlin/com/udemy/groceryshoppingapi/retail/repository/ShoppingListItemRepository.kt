package com.udemy.groceryshoppingapi.retail.repository

import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ShoppingListItemRepository : JpaRepository<ShoppingListItem, Long> {

    @Modifying(clearAutomatically = true)
    @Query("delete from ShoppingListItem s where s.id = ?1")
    fun deleteShoppingListItem(id: Long)
}