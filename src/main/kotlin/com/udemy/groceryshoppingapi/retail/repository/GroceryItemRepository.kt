package com.udemy.groceryshoppingapi.retail.repository

import com.udemy.groceryshoppingapi.retail.entity.GroceryItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface GroceryItemRepository : JpaRepository<GroceryItem, Long> {

    @Modifying(clearAutomatically = true)
    @Query("delete from GroceryItem g where g.id = ?1")
    fun deleteGroceryItem(id: Long)
}