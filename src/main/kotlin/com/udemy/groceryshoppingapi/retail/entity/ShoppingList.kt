package com.udemy.groceryshoppingapi.retail.entity

import com.udemy.groceryshoppingapi.user.entity.AppUser
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "shopping_list")
class ShoppingList(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shopping_list_id_seq")
    @SequenceGenerator(name = "shopping_list_id_seq", sequenceName = "shopping_list_id_seq", allocationSize = 1)
    val id: Long = 0,

    var receiptPictureUrl: String? = null,

    var isDone: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id")
    var appUser: AppUser? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supermarket_id")
    var supermarket: Supermarket? = null,

    @OneToMany(mappedBy = "shoppingList", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    var shoppingListItems: List<ShoppingListItem> = mutableListOf()
) {
    fun getTotalAmount(): Float {
        var totalAmount = 0f
        shoppingListItems.forEach { item -> totalAmount += item.price }
        return totalAmount
    }
}