package com.udemy.groceryshoppingapi.retail.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "shopping_list_item")
class ShoppingListItem(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shopping_list_item_id_seq")
    @SequenceGenerator(
        name = "shopping_list_item_id_seq",
        sequenceName = "shopping_list_item_id_seq",
        allocationSize = 1
    )
    val id: Long = 0,

    val quantity: Int = 0,

    val price: Float = 0.0F,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_list_id")
    var shoppingList: ShoppingList? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "grocery_item_id")
    var groceryItem: GroceryItem? = null
)