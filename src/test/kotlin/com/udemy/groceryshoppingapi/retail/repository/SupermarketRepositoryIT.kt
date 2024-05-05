package com.udemy.groceryshoppingapi.retail.repository

import com.udemy.groceryshoppingapi.dto.Hypermarket
import com.udemy.groceryshoppingapi.retail.entity.Supermarket
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class SupermarketRepositoryIT @Autowired constructor(
    val entityManager: TestEntityManager,
    val objectUnderTest: SupermarketRepository
) {

    private val supermarket = Supermarket(name = Hypermarket.ETSAN)

    @Test
    fun `when find by name is called then check if the name matches`() {
        entityManager.persist(supermarket)

        val actualSupermarket = objectUnderTest.findByName(supermarket.name)

        assertNotNull(actualSupermarket)
        assertEquals(supermarket.name, actualSupermarket?.name)
    }


    @Test
    fun `when find by name is called then expect null`() {
        val actualSupermarket = objectUnderTest.findByName(supermarket.name)

        assertNull(actualSupermarket)
    }

}