package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.dto.ShoppingListUpdateRequest
import com.udemy.groceryshoppingapi.error.BadRequestException
import com.udemy.groceryshoppingapi.error.ShoppingListNotFoundException
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import com.udemy.groceryshoppingapi.retail.entity.Supermarket
import com.udemy.groceryshoppingapi.retail.repository.ShoppingListRepository
import com.udemy.groceryshoppingapi.retail.util.ShoppingListItemMapper
import com.udemy.groceryshoppingapi.retail.util.ShoppingListMapper
import com.udemy.groceryshoppingapi.retail.util.SupermarketMapper
import com.udemy.groceryshoppingapi.user.entity.AppUser
import org.springframework.stereotype.Service

@Service
class ShoppingListServiceImpl(
    private val repository: ShoppingListRepository,
    private val mapper: ShoppingListMapper,
    private val supermarketMapper: SupermarketMapper,
    private val shoppingListItemMapper: ShoppingListItemMapper
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

    override fun getShoppingLists(appUser: AppUser, isDone: Boolean?): Set<ShoppingListResponse> {
        if (isDone != null) {
            val shoppingLists = repository.findAllByAppUserAndIsDone(appUser, isDone)
            return shoppingLists?.map { mapper.toDto(it) }?.toSet() ?: emptySet()
        }
        val shoppingLists = repository.findAllByAppUser(appUser)
        return shoppingLists?.map { mapper.toDto(it) }?.toSet() ?: emptySet()
    }

    override fun updateShoppingList(
        id: Long,
        updateRequest: ShoppingListUpdateRequest,
        appUser: AppUser
    ): ShoppingListResponse {
        val shoppingList: ShoppingList = validateShoppingList(id, appUser)
        val updatedSupermarket: Supermarket? =
            updateRequest.supermarket?.let { supermarketMapper.toEntity(it, appUser) }
        val updatedShoppingListItems: List<ShoppingListItem>? =
            updateRequest.shoppingListItems?.map { shoppingListItemMapper.toEntity(it, appUser) }
        shoppingList.apply {
            this.receiptPictureUrl = updateRequest.receiptPictureUrl ?: this.receiptPictureUrl
            this.supermarket = updatedSupermarket ?: this.supermarket
            this.shoppingListItems = updatedShoppingListItems ?: this.shoppingListItems
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