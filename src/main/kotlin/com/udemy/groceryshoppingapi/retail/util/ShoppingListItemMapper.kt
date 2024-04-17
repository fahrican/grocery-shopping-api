package com.udemy.groceryshoppingapi.retail.util

import com.udemy.groceryshoppingapi.dto.ShoppingListItemCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListItemResponse
import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import org.mapstruct.Mapper

@Mapper(componentModel = "spring", uses = [SupermarketMapper::class, GroceryItemMapper::class])
interface ShoppingListItemMapper {

    fun toDto(entity: ShoppingListItem): ShoppingListItemResponse

    fun toEntity(dto: ShoppingListItemCreateRequest): ShoppingListItem
}