package com.udemy.groceryshoppingapi.retail.entity

import com.udemy.groceryshoppingapi.dto.Category
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "grocery_item")
class GroceryItem(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grocery_item_id_seq")
    @SequenceGenerator(name = "grocery_item_id_seq", sequenceName = "grocery_item_id_seq", allocationSize = 1)
    val id: Long = 0,

    @NotBlank
    var name: String,

    @NotNull
    @Enumerated(EnumType.STRING)
    var category: Category = Category.OTHER
)