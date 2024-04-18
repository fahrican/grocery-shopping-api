package com.udemy.groceryshoppingapi.retail.util

import com.udemy.groceryshoppingapi.dto.SupermarketCreateRequest
import com.udemy.groceryshoppingapi.dto.SupermarketResponse
import com.udemy.groceryshoppingapi.retail.entity.Supermarket
import com.udemy.groceryshoppingapi.user.entity.AppUser
import org.springframework.stereotype.Component

@Component
class SupermarketMapper {

    fun toDto(entity: Supermarket): SupermarketResponse {
        return SupermarketResponse(
            id = entity.id,
            name = entity.name
        )
    }

    fun toEntity(dto: SupermarketCreateRequest, appUser: AppUser): Supermarket {
        return Supermarket(
            name = dto.name
        )
    }
}