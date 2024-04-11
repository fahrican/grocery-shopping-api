package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.error.BadRequestException
import com.udemy.groceryshoppingapi.error.ShoppingListNotFoundException
import com.udemy.groceryshoppingapi.retail.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.retail.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.retail.dto.ShoppingListUpdateRequest
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.repository.ShoppingListRepository
import com.udemy.groceryshoppingapi.retail.util.ShoppingListMapper
import com.udemy.groceryshoppingapi.user.entity.AppUser
import org.springframework.stereotype.Service

@Service
class ShoppingListServiceImpl(
    private val repository: ShoppingListRepository,
    private val mapper: ShoppingListMapper
) : ShoppingListService {
    override fun createShoppingList(createRequest: ShoppingListCreateRequest, appUser: AppUser): ShoppingListResponse {
        if (createRequest.shoppingListItems.isEmpty()) {
            throw BadRequestException("A shopping list must have at least one item")
        }
        val shoppingList = mapper.toEntity(createRequest, appUser)
        val entity = repository.save(shoppingList)
        return mapper.toDto(entity)
    }

    override fun getShoppingListById(id: Long, appUser: AppUser): ShoppingListResponse {
        val shoppingList: ShoppingList = validateShoppingList(id, appUser)
        return mapper.toDto(shoppingList)
    }

    override fun getShoppingLists(appUser: AppUser): Set<ShoppingListResponse> {
        val shoppingLists = repository.findAllByAppUser(appUser)
        return shoppingLists?.map { mapper.toDto(it) }?.toSet() ?: emptySet()
    }

    override fun updateShoppingList(
        id: Long,
        updateRequest: ShoppingListUpdateRequest,
        appUser: AppUser
    ): ShoppingListResponse {
        val shoppingList: ShoppingList = validateShoppingList(id, appUser)
        shoppingList.apply {
            this.receiptPictureUrl = updateRequest.receiptPictureUrl ?: receiptPictureUrl
            this.supermarket = updateRequest.supermarket ?: supermarket
            this.shoppingListItems = updateRequest.shoppingListItems ?: shoppingListItems
        }
        val entity = repository.save(shoppingList)
        return mapper.toDto(entity)
    }

    override fun deleteShoppingList(id: Long, appUser: AppUser) {
        validateShoppingList(id, appUser)
        repository.deleteById(id)
    }

    private fun validateShoppingList(id: Long, appUser: AppUser): ShoppingList {
        val shoppingList = repository.findByIdAndAppUser(id, appUser)
            ?: throw ShoppingListNotFoundException(message = "Task with ID: $id does not exist!")
        return shoppingList
    }
}