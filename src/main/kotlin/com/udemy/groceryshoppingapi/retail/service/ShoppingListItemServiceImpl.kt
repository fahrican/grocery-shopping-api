package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.ShoppingListItemCreateRequest
import com.udemy.groceryshoppingapi.retail.entity.GroceryItem
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import com.udemy.groceryshoppingapi.retail.repository.ShoppingListItemRepository
import com.udemy.groceryshoppingapi.retail.util.GroceryItemMapper
import com.udemy.groceryshoppingapi.retail.util.ShoppingListItemMapper
import org.springframework.stereotype.Service

@Service
class ShoppingListItemServiceImpl(
    private val mapper: ShoppingListItemMapper,
    private val repository: ShoppingListItemRepository,
    private val groceryItemMapper: GroceryItemMapper,
    private val groceryItemService: GroceryItemService
) : ShoppingListItemService {

    override fun createShoppingListItem(
        createRequest: ShoppingListItemCreateRequest,
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

    override fun updateShoppingList(
        shoppingList: ShoppingList,
        shoppingListItems: List<ShoppingListItem>
    ): List<ShoppingListItem> {
        shoppingListItems.forEach { it.shoppingList = shoppingList }
        return repository.saveAllAndFlush(shoppingListItems)
    }

    override fun deleteShoppingListItems(shoppingListItems: List<ShoppingListItem>) {
        val groceryItems: List<GroceryItem> = shoppingListItems.mapNotNull { it.groceryItem }
        repository.deleteAll(shoppingListItems)
        if (groceryItems.isNotEmpty()) {
            groceryItemService.deleteGroceryItems(groceryItems)
        }
    }

    override fun updateShoppingListItems(
        shoppingList: ShoppingList,
        shoppingListItems: List<ShoppingListItemCreateRequest>
    ): List<ShoppingListItem> {
        val updatedShoppingListItems: MutableList<ShoppingListItem> = mutableListOf()
        repository.deleteAll(shoppingList.shoppingListItems)
        shoppingListItems.forEach {
            val groceryItem = groceryItemMapper.toEntity(it.groceryItem)
            val shoppingListItem = mapper.toEntity(it, shoppingList, groceryItem)
            updatedShoppingListItems.add(shoppingListItem)
        }
        return repository.saveAll(updatedShoppingListItems)
    }
}