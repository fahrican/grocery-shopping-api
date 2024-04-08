package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.retail.repository.ShoppingListRepository
import org.springframework.stereotype.Service

@Service
class ShoppingListServiceImpl(private val shoppingListRepository: ShoppingListRepository) : ShoppingListService {

    override fun createShoppingList() {

    }
}