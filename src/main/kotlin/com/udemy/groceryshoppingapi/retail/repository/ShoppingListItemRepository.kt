package com.udemy.groceryshoppingapi.retail.repository

import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ShoppingListItemRepository: JpaRepository<ShoppingListItem, Long> {
}