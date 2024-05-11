package com.udemy.groceryshoppingapi.retail.service

import com.udemy.groceryshoppingapi.dto.Hypermarket
import com.udemy.groceryshoppingapi.error.SupermarketException
import com.udemy.groceryshoppingapi.retail.entity.Supermarket
import com.udemy.groceryshoppingapi.retail.repository.SupermarketRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SupermarketServiceImplTest {

    private val mockRepository = mockk<SupermarketRepository>(relaxed = true)

    private val objectUnderTest = SupermarketServiceImpl(mockRepository)

    private val lidl = Hypermarket.LIDL

    @Test
    fun `when find supermarket by name is called then expect lidl as supermarket`() {
        val expectedSupermarket = Supermarket(name = lidl)
        every { mockRepository.findByName(lidl) } returns expectedSupermarket

        val actualResult: Supermarket = objectUnderTest.findSupermarketByName(lidl)

        assertEquals(expectedSupermarket, actualResult)
    }

    @Test
    fun `when find supermarket by name is called then expect supermarket exception`() {
        every { mockRepository.findByName(any()) } returns null

        val actualException: SupermarketException =
            assertThrows<SupermarketException> { objectUnderTest.findSupermarketByName(lidl) }

        assertEquals("Supermarket $lidl does not exist!", actualException.message)
    }
}