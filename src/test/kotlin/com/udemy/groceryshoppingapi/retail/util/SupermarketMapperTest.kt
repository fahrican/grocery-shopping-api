package com.udemy.groceryshoppingapi.retail.util

import com.udemy.groceryshoppingapi.dto.Hypermarket
import com.udemy.groceryshoppingapi.dto.SupermarketCreateRequest
import com.udemy.groceryshoppingapi.retail.entity.Supermarket
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SupermarketMapperTest {

    private val objectUnderTest = SupermarketMapper()

    @Test
    fun `when to dto is called then expect the fields match`() {
        val supermarket = Supermarket(id = 1L, name = Hypermarket.LIDL)

        val dto = objectUnderTest.toDto(supermarket)

        assertEquals(supermarket.id, dto.id)
        assertEquals(supermarket.name, dto.market)
    }

    @Test
    fun `when to entity is called then expect the fields match`() {
        val createRequest = SupermarketCreateRequest(market = Hypermarket.SPAR)

        val entity = objectUnderTest.toEntity(createRequest)

        assertEquals(createRequest.market, entity.name)
    }
}
