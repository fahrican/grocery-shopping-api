package com.udemy.groceryshoppingapi.retail.repository

import com.udemy.groceryshoppingapi.dto.Category
import com.udemy.groceryshoppingapi.retail.entity.GroceryItem
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull


@DataJpaTest
class GroceryItemRepositoryIT @Autowired constructor(
    val entityManager: TestEntityManager,
    val objectUnderTest: GroceryItemRepository
) {

    private val groceryItem = GroceryItem(name = "Apple", category = Category.FRUITS)

    @BeforeEach
    fun setUp() {
        entityManager.persist(groceryItem)
    }

    @AfterEach
    fun tearDown() {
        entityManager.clear()
    }

    @Test
    fun `when grocery item gets persisted then check if it can be found after deleting it`() {
        objectUnderTest.deleteGroceryItem(groceryItem.id)

        val item = objectUnderTest.findByIdOrNull(groceryItem.id)
        assertNull(item, "Item should be deleted")
    }

    @Test
    fun `when grocery item gets deleted then check amount of items in repository`() {
        val secondGroceryItem = GroceryItem(name = "Banana", category = Category.FRUITS)
        entityManager.persist(secondGroceryItem)

        objectUnderTest.deleteGroceryItem(secondGroceryItem.id)
        val grocerItems: List<GroceryItem> = objectUnderTest.findAll()

        assertEquals(1, grocerItems.size)
    }
}