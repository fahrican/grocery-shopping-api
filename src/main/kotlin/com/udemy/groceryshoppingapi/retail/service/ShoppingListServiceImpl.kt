package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.error.BadRequestException
import com.udemy.groceryshoppingapi.retail.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.retail.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.retail.dto.ShoppingListUpdateRequest
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

    override fun getShoppingLists(appUser: AppUser): Set<ShoppingListResponse> {
        TODO("Not yet implemented")
    }

    override fun getShoppingListById(id: Long, appUser: AppUser): ShoppingListResponse {
        TODO("Not yet implemented")
    }

    override fun updateShoppingList(
        id: Long,
        updateRequest: ShoppingListUpdateRequest,
        appUser: AppUser
    ): ShoppingListResponse {
        TODO("Not yet implemented")
    }

    override fun deleteShoppingList(id: Long, appUser: AppUser): String {
        TODO("Not yet implemented")
    }
}