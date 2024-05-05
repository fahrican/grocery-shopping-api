package com.udemy.groceryshoppingapi.retail.repository

import com.udemy.groceryshoppingapi.retail.entity.ShoppingListItem
import org.junit.jupiter.api.Assertions
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

    @Test
    fun `when shopping list item gets persisted then check if it can be found after deleting it`() {
        entityManager.persist(listItem)
        objectUnderTest.deleteShoppingListItem(listItem.id)

        val item = objectUnderTest.findByIdOrNull(listItem.id)
        Assertions.assertNull(item, "List item should be deleted")
    }
}