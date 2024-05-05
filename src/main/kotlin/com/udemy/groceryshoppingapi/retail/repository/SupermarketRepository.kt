package com.udemy.groceryshoppingapi.retail.repository

import com.udemy.groceryshoppingapi.dto.Hypermarket
import com.udemy.groceryshoppingapi.retail.entity.Supermarket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface SupermarketRepository : JpaRepository<Supermarket, Long> {

    @Query("SELECT s FROM Supermarket s WHERE s.name = ?1")
    fun findByName(name: Hypermarket): Supermarket?
}