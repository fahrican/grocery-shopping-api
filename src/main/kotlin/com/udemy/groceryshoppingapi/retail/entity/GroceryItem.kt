package com.udemy.groceryshoppingapi.retail.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "grocery_item")
class GroceryItem(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grocery_item_id_seq")
    @SequenceGenerator(name = "grocery_item_id_seq", sequenceName = "grocery_item_id_seq", allocationSize = 1)
    val id: Long = 0,

    @NotBlank
    val name: String,

    @ManyToOne(fetch = FetchType.LAZY)
    val groceryCategory: GroceryCategory? = null
)