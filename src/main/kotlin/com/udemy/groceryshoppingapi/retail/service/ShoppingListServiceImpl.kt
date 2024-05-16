package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.GroceryItemResponse
import com.udemy.groceryshoppingapi.dto.GroceryItemUpdateRequest
import com.udemy.groceryshoppingapi.dto.Hypermarket
import com.udemy.groceryshoppingapi.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemResponse
import com.udemy.groceryshoppingapi.dto.ShoppingListItemUpdateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.dto.ShoppingListUpdateRequest
import com.udemy.groceryshoppingapi.dto.SupermarketResponse
import com.udemy.groceryshoppingapi.error.BadRequestException
import com.udemy.groceryshoppingapi.error.ShoppingListItemNotFoundException
import com.udemy.groceryshoppingapi.error.ShoppingListNotFoundException
import com.udemy.groceryshoppingapi.error.SupermarketException
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import com.udemy.groceryshoppingapi.retail.entity.Supermarket
import com.udemy.groceryshoppingapi.retail.repository.ShoppingListRepository
import com.udemy.groceryshoppingapi.retail.util.ShoppingListItemMapper
import com.udemy.groceryshoppingapi.retail.util.ShoppingListMapper
import com.udemy.groceryshoppingapi.retail.util.SupermarketMapper
import com.udemy.groceryshoppingapi.user.entity.AppUser
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ShoppingListServiceImpl(
    private val shoppingListMapper: ShoppingListMapper,
    private val repository: ShoppingListRepository,
    private val supermarketMapper: SupermarketMapper,
    private val supermarketService: SupermarketService,
    private val shoppingListItemMapper: ShoppingListItemMapper,
    private val shoppingListItemService: ShoppingListItemService,
    private val groceryItemService: GroceryItemService
) : ShoppingListService {

    @Transactional
    override fun createShoppingList(createRequest: ShoppingListCreateRequest, appUser: AppUser): ShoppingListResponse {
        validateRequest(createRequest)
        val supermarket = supermarketService.findSupermarketByName(createRequest.supermarket.market)
        val shoppingList = createAndSaveShoppingList(createRequest, supermarket, appUser)
        val shoppingListItems = createAndAttachShoppingListItems(createRequest, shoppingList)
        return generateShoppingListResponse(supermarket, shoppingListItems, shoppingList)
    }

    override fun getShoppingListById(id: Long, appUser: AppUser): ShoppingListResponse {
        val shoppingList: ShoppingList = validateShoppingList(id, appUser)
        val supermarket = shoppingList.supermarket?.let { supermarketMapper.toDto(it) }
            ?: throw SupermarketException("There is no supermarket associated with this shopping list!")
        val shoppingListItems: List<ShoppingListItemResponse> = shoppingList.shoppingListItems.map {
            shoppingListItemMapper.toDto(it)
        }
        return shoppingListMapper.toDto(shoppingList, supermarket, shoppingListItems)
    }

    override fun getShoppingLists(appUser: AppUser, isDone: Boolean?): Set<ShoppingListResponse> {
        val shoppingLists = fetchShoppingLists(appUser, isDone)
        return shoppingLists.map(this::mapToShoppingListResponse).toSet()
    }

    @Transactional
    override fun updateShoppingList(
        id: Long,
        updateRequest: ShoppingListUpdateRequest,
        appUser: AppUser
    ): ShoppingListResponse {
        val shoppingList = validateShoppingList(id, appUser)
        val updatedSupermarket: Supermarket? = if (updateRequest.supermarket != null) {
            updateRequest.supermarket.market?.let { supermarketService.findSupermarketByName(it) }
        } else {
            shoppingList.supermarket ?: Supermarket(name = Hypermarket.OTHER)
        }
        shoppingList.apply {
            receiptPictureUrl = updateRequest.receiptPictureUrl ?: receiptPictureUrl
            isDone = updateRequest.isDone ?: isDone
            supermarket = updatedSupermarket ?: supermarket
        }
        val entity: ShoppingList = repository.save(shoppingList)
        return generateShoppingListResponse(updatedSupermarket!!, shoppingList.shoppingListItems, entity)
    }

    @Transactional
    override fun deleteShoppingList(id: Long, appUser: AppUser) {
        val shoppingList = validateShoppingList(id, appUser)
        shoppingListItemService.deleteShoppingListItems(shoppingList.shoppingListItems)
        repository.delete(shoppingList)
    }

    override fun getGroceryItem(listId: Long, listItemId: Long, appUser: AppUser): GroceryItemResponse {
        val shoppingList = this.getShoppingListById(listId, appUser)
        val shoppingListItem: ShoppingListItemResponse = retrieveListItemResponse(shoppingList, listItemId)
        return shoppingListItem.groceryItem
    }

    override fun updateGroceryItem(groceryId: Long, updateRequest: GroceryItemUpdateRequest): GroceryItemResponse {
        val groceryItem: GroceryItemResponse = groceryItemService.updateGroceryItem(groceryId, updateRequest)
        return groceryItem
    }

    override fun getShoppingListItems(listId: Long, appUser: AppUser): Set<ShoppingListItemResponse> {
        val shoppingList: ShoppingList = validateShoppingList(listId, appUser)
        return shoppingList.shoppingListItems.map { shoppingListItemMapper.toDto(it) }.toSet()
    }

    override fun getShoppingListItem(listId: Long, itemId: Long, appUser: AppUser): ShoppingListItemResponse {
        val shoppingList = this.getShoppingListById(listId, appUser)
        return retrieveListItemResponse(shoppingList, itemId)
    }

    override fun createShoppingListItem(
        listId: Long,
        createRequest: ShoppingListItemCreateRequest,
        appUser: AppUser
    ): ShoppingListItemResponse {
        val shoppingList: ShoppingList = validateShoppingList(listId, appUser)
        val shoppingListItem: ShoppingListItem = shoppingListItemService.createShoppingListItem(
            createRequest,
            shoppingList
        )
        return shoppingListItemMapper.toDto(shoppingListItem)
    }

    @Transactional
    override fun deleteShoppingListItem(listId: Long, itemId: Long, appUser: AppUser) {
        val shoppingList = repository.findByIdAndUser(listId, appUser)
        val listItem: ShoppingListItem = shoppingList?.shoppingListItems?.find { it.id == itemId }
            ?: throw ShoppingListItemNotFoundException(message = "Shopping list item with ID: $itemId does not exist!")
        shoppingListItemService.deleteShoppingListItem(itemId)
        listItem.groceryItem?.let { groceryItemService.deleteGroceryItem(it.id) }
    }

    override fun updateShoppingListItem(
        listId: Long,
        itemId: Long,
        updateRequest: ShoppingListItemUpdateRequest,
        appUser: AppUser
    ): ShoppingListItemResponse {
        val shoppingListItem: ShoppingListItem = shoppingListItemService.updateShoppingListItem(
            itemId,
            updateRequest
        )
        return shoppingListItemMapper.toDto(shoppingListItem)
    }

    private fun validateShoppingList(id: Long, appUser: AppUser): ShoppingList {
        val shoppingList = repository.findByIdAndUser(id, appUser)
            ?: throw ShoppingListNotFoundException(message = "Shopping list with ID: $id does not exist!")
        return shoppingList
    }

    private fun generateShoppingListResponse(
        supermarket: Supermarket,
        shoppingListItems: List<ShoppingListItem>,
        entity: ShoppingList
    ): ShoppingListResponse {
        val supermarketResponse: SupermarketResponse = supermarketMapper.toDto(supermarket)
        val shoppingListItemResponses: List<ShoppingListItemResponse> = shoppingListItems.map {
            shoppingListItemMapper.toDto(it)
        }
        val shoppingListResponse: ShoppingListResponse =
            shoppingListMapper.toDto(entity, supermarketResponse, shoppingListItemResponses)
        shoppingListItemResponses.forEach { _ ->
            shoppingListItems.map { listItem ->
                shoppingListItemMapper.toDto(listItem)
            }
        }
        return shoppingListResponse
    }

    private fun validateRequest(createRequest: ShoppingListCreateRequest) {
        if (createRequest.shoppingListItems.isEmpty()) {
            throw BadRequestException("A shopping list must have at least one item")
        }
    }

    private fun createAndSaveShoppingList(
        createRequest: ShoppingListCreateRequest,
        supermarket: Supermarket,
        appUser: AppUser
    ): ShoppingList {
        val shoppingList = shoppingListMapper.toEntity(createRequest, supermarket, emptyList(), appUser)
        return repository.save(shoppingList)
    }

    private fun createAndAttachShoppingListItems(
        createRequest: ShoppingListCreateRequest,
        shoppingList: ShoppingList
    ): List<ShoppingListItem> {
        val shoppingListItems = createRequest.shoppingListItems.map {
            shoppingListItemService.createShoppingListItem(it, shoppingList)
        }
        return shoppingListItemService.updateShoppingList(shoppingList, shoppingListItems)
    }

    private fun retrieveListItemResponse(
        shoppingList: ShoppingListResponse,
        listItemId: Long
    ): ShoppingListItemResponse {
        return shoppingList.shoppingListItems?.firstOrNull { it.id == listItemId }
            ?: throw BadRequestException("Shopping list item with ID: $listItemId does not exist!")
    }

    private fun fetchShoppingLists(appUser: AppUser, isDone: Boolean?): List<ShoppingList> {
        return if (isDone != null) {
            repository.findAllByUserAndIsDone(appUser, isDone) ?: emptyList()
        } else {
            repository.findAllByUser(appUser) ?: emptyList()
        }
    }

    private fun mapToShoppingListResponse(shoppingList: ShoppingList): ShoppingListResponse {
        val supermarketResponse = shoppingList.supermarket?.let(supermarketMapper::toDto) ?: SupermarketResponse()
        val shoppingListItems = shoppingList.shoppingListItems.map(shoppingListItemMapper::toDto)
        return shoppingListMapper.toDto(shoppingList, supermarketResponse, shoppingListItems)
    }
}