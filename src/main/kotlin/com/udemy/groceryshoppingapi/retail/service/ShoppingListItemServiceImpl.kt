package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.Category
import com.udemy.groceryshoppingapi.dto.ShoppingListItemCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemUpdateRequest
import com.udemy.groceryshoppingapi.retail.entity.GroceryItem
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import com.udemy.groceryshoppingapi.retail.repository.ShoppingListItemRepository
import com.udemy.groceryshoppingapi.retail.util.GroceryItemMapper
import org.springframework.stereotype.Service

@Service
class ShoppingListItemServiceImpl(
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

    override fun getShoppingListItem(id: Long): ShoppingListItem {
        return repository.findById(id).orElseThrow()
    }

    override fun deleteShoppingListItem(id: Long) {
        repository.deleteById(id)
    }

    override fun updateShoppingListItem(id: Long, updateRequest: ShoppingListItemUpdateRequest): ShoppingListItem {
        val shoppingListItem = repository.findById(id).orElseThrow()
        var updatedGroceryItem: GroceryItem? = null
        if (updateRequest.groceryItem?.name != null || updateRequest.groceryItem?.category != null) {
            updatedGroceryItem = GroceryItem(
                name = updateRequest.groceryItem.name ?: "",
                category = updateRequest.groceryItem.category ?: Category.OTHER
            )
        }
        shoppingListItem.apply {
            quantity = updateRequest.quantity ?: quantity
            price = updateRequest.price ?: price
            groceryItem = updatedGroceryItem ?: groceryItem
        }
        return repository.save(shoppingListItem)
    }
}