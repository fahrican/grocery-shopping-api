package com.udemy.groceryshoppingapi.retail.repository

import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
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
class ShoppingListItemRepositoryIT @Autowired constructor(
    val entityManager: TestEntityManager,
    val objectUnderTest: ShoppingListItemRepository
) {

    private val listItem = ShoppingListItem()

    @BeforeEach
    fun setUp() {
        entityManager.persist(listItem)
    }

    @AfterEach
    fun tearDown() {
        entityManager.clear()
    }

    @Test
    fun `when shopping list item gets persisted then check if it can be found after deleting it`() {
        objectUnderTest.deleteShoppingListItem(listItem.id)

        val item = objectUnderTest.findByIdOrNull(listItem.id)
        assertNull(item, "List item should be deleted")
    }

    @Test
    fun `when shopping list item gets deleted then check for amount of items in the repository`() {
        val secondListItem = ShoppingListItem()
        entityManager.persist(secondListItem)

        objectUnderTest.deleteShoppingListItem(listItem.id)
        val listItems: List<ShoppingListItem> = objectUnderTest.findAll()

        assertEquals(1, listItems.size)
    }
}