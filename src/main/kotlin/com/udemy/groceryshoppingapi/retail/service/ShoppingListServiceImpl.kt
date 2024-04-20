package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.dto.ShoppingListUpdateRequest
import com.udemy.groceryshoppingapi.dto.SupermarketResponse
import com.udemy.groceryshoppingapi.error.BadRequestException
import com.udemy.groceryshoppingapi.error.ShoppingListNotFoundException
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
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
    private val supermarketRepository: SupermarketRepository,
    private val groceryItemService: GroceryItemService,
    private val shoppingListItemService: ShoppingListItemService
) : ShoppingListService {


    @Transactional
    override fun createShoppingList(createRequest: ShoppingListCreateRequest, appUser: AppUser): ShoppingListResponse {
        if (createRequest.shoppingListItems.isEmpty()) {
            throw BadRequestException("A shopping list must have at least one item")
        }

        //createRequest.shoppingListItems.map { groceryItemService.createGroceryItem(it.groceryItem, appUser) }

        var shoppingListItems: List<ShoppingListItem> = createRequest.shoppingListItems.map {
            shoppingListItemService.createShoppingListItem(it, appUser, null)
        }

        val supermarket = supermarketRepository.findByName(createRequest.supermarket.name)
            ?: throw BadRequestException("Supermarket ${createRequest.supermarket.name} does not exist!")

        val shoppingList = shoppingListMapper.toEntity(createRequest, supermarket, shoppingListItems, appUser)
        val entity = shoppingListRepository.save(shoppingList)
        shoppingListItems = shoppingListItemService.updateShoppingList(appUser, shoppingList, shoppingListItems)

        // set up dtos
        val supermarketResponse = supermarketMapper.toDto(supermarket)
        var shoppingListItemsResponse =
            shoppingListItems.map { shoppingListItemMapper.toDto(it, null) }
        val shoppingListResponse = shoppingListMapper.toDto(entity, supermarketResponse, shoppingListItemsResponse)
        shoppingListItemsResponse =
            shoppingListItems.map { shoppingListItemMapper.toDto(it, shoppingListResponse) }
        return shoppingListResponse
    }

    override fun getShoppingListById(id: Long, appUser: AppUser): ShoppingListResponse {
        val shoppingList: ShoppingList = validateShoppingList(id, appUser)
        val supermarket = supermarketMapper.toDto(shoppingList.supermarket!!)
        val shoppingListItems = shoppingList.shoppingListItems.map { shoppingListItemMapper.toDto(it, null) }
        return shoppingListMapper.toDto(shoppingList, supermarket, shoppingListItems)
    }

    override fun getShoppingLists(appUser: AppUser, isDone: Boolean?): Set<ShoppingListResponse> {
        /*     if (isDone != null) {
                 val shoppingLists = shoppingListRepository.findAllByAppUserAndIsDone(appUser, isDone)
                 return shoppingLists?.map { shoppingListMapper.toDto(it) }
                     ?.toSet() ?: emptySet()
             }
             val shoppingLists = shoppingListRepository.findAllByAppUser(appUser)
             return shoppingLists?.map { shoppingListMapper.toDto(it) }?.toSet()
                 ?: emptySet()*/

        return emptySet()
    }

    override fun updateShoppingList(
        id: Long,
        updateRequest: ShoppingListUpdateRequest,
        appUser: AppUser
    ): ShoppingListResponse {
        /*val shoppingList: ShoppingList = validateShoppingList(id, appUser)
        val updatedSupermarket: Supermarket? =
            updateRequest.supermarket?.let { supermarketMapper.toEntity(it) }
        val updatedShoppingListItems: List<ShoppingListItem>? =
            updateRequest.shoppingListItems?.map { shoppingListItemMapper.toEntity(it) }
        shoppingList.apply {
            this.receiptPictureUrl = updateRequest.receiptPictureUrl ?: this.receiptPictureUrl
            this.supermarket = updatedSupermarket ?: this.supermarket
            this.shoppingListItems = updatedShoppingListItems ?: this.shoppingListItems
        }
        val entity = shoppingListRepository.save(shoppingList)
        return shoppingListMapper.toDto(entity)*/

        return ShoppingListResponse(0, "", false, SupermarketResponse(), emptyList(), 0f)
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