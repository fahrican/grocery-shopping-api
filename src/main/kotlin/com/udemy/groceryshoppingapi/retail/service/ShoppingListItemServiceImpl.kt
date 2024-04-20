package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.ShoppingListItemCreateRequest
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import com.udemy.groceryshoppingapi.retail.repository.ShoppingListItemRepository
import com.udemy.groceryshoppingapi.retail.util.GroceryItemMapper
import com.udemy.groceryshoppingapi.user.entity.AppUser
import org.springframework.stereotype.Service

@Service
class ShoppingListItemServiceImpl(
    private val repository: ShoppingListItemRepository,
    private val groceryItemMapper: GroceryItemMapper
) : ShoppingListItemService {

    override fun createShoppingListItem(
        createRequest: ShoppingListItemCreateRequest,
        appUser: AppUser,
        shoppingList: ShoppingList?
    ): ShoppingListItem {
        val groceryItem = groceryItemMapper.toEntity(createRequest.groceryItem)
        val shoppingListItem = ShoppingListItem(
            quantity = createRequest.quantity,
            price = createRequest.price,
            shoppingList = shoppingList,
            groceryItem = groceryItem
        )
        return repository.save(shoppingListItem)
    }
}