package com.udemy.groceryshoppingapi.retail.repository

import com.udemy.groceryshoppingapi.dto.Hypermarket
import com.udemy.groceryshoppingapi.retail.entity.Supermarket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface SupermarketRepository : JpaRepository<Supermarket, Long> {

    fun findByName(name: Hypermarket): Supermarket?
}