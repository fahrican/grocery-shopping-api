package com.udemy.groceryshoppingapi.retail.repository

import com.udemy.groceryshoppingapi.dto.Hypermarket
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import com.udemy.groceryshoppingapi.retail.entity.Supermarket
import com.udemy.groceryshoppingapi.user.entity.AppUser
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class ShoppingListRepositoryIT @Autowired constructor(
    val entityManager: TestEntityManager,
    val objectUnderTest: ShoppingListRepository
) {

    private val appUser = AppUser(
        firstName = "Ibo",
        lastName = "Al Orabi",
        email = "ibo@orabi.com",
        appUsername = "ibo-orabi",
        appPassword = "password123",
        isVerified = true
    )
    private val supermarket = Supermarket(name = Hypermarket.BILLA)
    private lateinit var shoppingList: ShoppingList

    @BeforeEach
    fun setUp() {
        entityManager.persist(appUser)
        entityManager.persist(supermarket)
        shoppingList = ShoppingList(
            receiptPictureUrl = "https://example.com/receipt.jpg",
            isDone = false,
            appUser = appUser,
            supermarket = supermarket
        )
        entityManager.persist(shoppingList)
        entityManager.flush()
    }

    @AfterEach
    fun tearDown() {
        entityManager.clear()
    }

    @Test
    fun `when find all by user is called then check if the list is not empty`() {
        val actualShoppingLists: List<ShoppingList>? = objectUnderTest.findAllByUser(appUser)

        assertNotNull(actualShoppingLists)
        assertEquals(1, actualShoppingLists?.size)
    }

    @Test
    fun `when entities got cleared and find all by user is called then check if empty list comes back`() {
        entityManager.remove(shoppingList)
        val actualShoppingLists: List<ShoppingList>? = objectUnderTest.findAllByUser(appUser)

        assertNotNull(actualShoppingLists)
        assertEquals(0, actualShoppingLists?.size)
    }

    @Test
    fun `when find by id and user is called then check for shopping list properties`() {
        val actualShoppingList: ShoppingList? = objectUnderTest.findByIdAndUser(shoppingList.id, appUser)

        assertNotNull(actualShoppingList)
        assertEquals(shoppingList.id, actualShoppingList?.id)
        assertEquals(shoppingList.appUser, actualShoppingList?.appUser)
        assertEquals(shoppingList.supermarket, actualShoppingList?.supermarket)
    }

    @Test
    fun `when find by id and user is called on a not existing id then expect null`() {
        val actualShoppingList: ShoppingList? = objectUnderTest.findByIdAndUser(999, appUser)

        assertNull(actualShoppingList)
    }

    @Test
    fun `when find all by user and is done is called for false then check if the list is not empty`() {
        val actualShoppingLists: List<ShoppingList>? = objectUnderTest.findAllByUserAndIsDone(appUser, false)

        assertNotNull(actualShoppingLists)
        assertEquals(1, actualShoppingLists?.size)
    }

    @Test
    fun `when find all by user and is done is called for true then check if the list is empty`() {
        val actualShoppingLists: List<ShoppingList>? = objectUnderTest.findAllByUserAndIsDone(appUser, true)

        assertNotNull(actualShoppingLists)
        assertEquals(0, actualShoppingLists?.size)
    }
}