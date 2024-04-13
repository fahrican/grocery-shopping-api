package com.udemy.groceryshoppingapi.retail.util

import com.udemy.groceryshoppingapi.dto.ShoppingListCreateRequest
import com.udemy.groceryshoppingapi.dto.ShoppingListResponse
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [SupermarketMapper::class, ShoppingListItemMapper::class])
interface ShoppingListMapper {
    fun toDto(entity: ShoppingList?): ShoppingListResponse

    @Mapping(target = "shoppingListItems", source = "shoppingListItems")
    fun toEntity(dto: ShoppingListCreateRequest): ShoppingList
}