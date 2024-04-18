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
import com.udemy.groceryshoppingapi.retail.repository.SupermarketRepository
import com.udemy.groceryshoppingapi.retail.util.ShoppingListItemMapper
import com.udemy.groceryshoppingapi.retail.util.ShoppingListMapper
import com.udemy.groceryshoppingapi.retail.util.SupermarketMapper
import com.udemy.groceryshoppingapi.user.entity.AppUser
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ShoppingListServiceImpl(
    private val shoppingListMapper: ShoppingListMapper,
    private val shoppingListItemMapper: ShoppingListItemMapper,
    private val supermarketMapper: SupermarketMapper,
    private val shoppingListRepository: ShoppingListRepository,
    private val supermarketRepository: SupermarketRepository
) : ShoppingListService {

    @Transactional
    override fun createShoppingList(createRequest: ShoppingListCreateRequest, appUser: AppUser): ShoppingListResponse {
        if (createRequest.shoppingListItems.isEmpty()) {
            throw BadRequestException("A shopping list must have at least one item")
        }

        // Fetch or verify the supermarket before mapping and saving
        val supermarket = supermarketRepository.findByName(createRequest.supermarket.name.name)

        // Map the DTO to entity
        val shoppingList = shoppingListMapper.toEntity(createRequest)
        shoppingList.supermarket = supermarket // Ensure the supermarket is attached
        shoppingList.appUser = appUser // Assign the user

        // Save the entity
        val entity = shoppingListRepository.save(shoppingList)

        // Return the mapped DTO
        return shoppingListMapper.toDto(entity)
    }

    override fun getShoppingListById(id: Long, appUser: AppUser): ShoppingListResponse {
        val shoppingList: ShoppingList = validateShoppingList(id, appUser)
        return shoppingListMapper.toDto(shoppingList)
    }

    override fun getShoppingLists(appUser: AppUser, isDone: Boolean?): Set<ShoppingListResponse> {
        if (isDone != null) {
            val shoppingLists = shoppingListRepository.findAllByAppUserAndIsDone(appUser, isDone)
            return shoppingLists?.map { shoppingListMapper.toDto(it) }?.toSet() ?: emptySet()
        }
        val shoppingLists = shoppingListRepository.findAllByAppUser(appUser)
        return shoppingLists?.map { shoppingListMapper.toDto(it) }?.toSet() ?: emptySet()
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
            updateRequest.shoppingListItems?.map { shoppingListItemMapper.toEntity(it) }
        shoppingList.apply {
            this.receiptPictureUrl = updateRequest.receiptPictureUrl ?: this.receiptPictureUrl
            this.supermarket = updatedSupermarket ?: this.supermarket
            this.shoppingListItems = updatedShoppingListItems ?: this.shoppingListItems
        }
        val entity = shoppingListRepository.save(shoppingList)
        return shoppingListMapper.toDto(entity)
    }

    override fun deleteShoppingList(id: Long, appUser: AppUser) {
        validateShoppingList(id, appUser)
        shoppingListRepository.deleteById(id)
    }

    private fun validateShoppingList(id: Long, appUser: AppUser): ShoppingList {
        val shoppingList = shoppingListRepository.findByIdAndAppUser(id, appUser)
            ?: throw ShoppingListNotFoundException(message = "Task with ID: $id does not exist!")
        return shoppingList
    }
}