package com.udemy.groceryshoppingapi.retail.entity

import com.udemy.groceryshoppingapi.retail.Category
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "grocery_category")
class GroceryCategory(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grocery_category_id_seq")
    @SequenceGenerator(name = "grocery_category_id_seq", sequenceName = "grocery_category_id_seq", allocationSize = 1)
    val id: Long = 0,

    @NotNull
    @Enumerated(EnumType.STRING)
    val name: Category = Category.OTHER
)