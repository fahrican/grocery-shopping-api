package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.Hypermarket
import com.udemy.groceryshoppingapi.retail.entity.Supermarket

interface SupermarketService {

    fun findSupermarketByName(name: Hypermarket): Supermarket
}