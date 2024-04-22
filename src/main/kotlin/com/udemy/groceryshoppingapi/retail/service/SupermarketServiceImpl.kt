package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.Hypermarket
import com.udemy.groceryshoppingapi.error.SupermarketException
import com.udemy.groceryshoppingapi.retail.entity.Supermarket
import com.udemy.groceryshoppingapi.retail.repository.SupermarketRepository
import org.springframework.stereotype.Service

@Service
class SupermarketServiceImpl(private val repository: SupermarketRepository) : SupermarketService {

    override fun findSupermarketByName(name: Hypermarket): Supermarket {
        return repository.findByName(name) ?: throw SupermarketException("Supermarket $name does not exist!")
    }
}