package com.udemy.groceryshoppingapi.retail.entity

import com.udemy.groceryshoppingapi.dto.Hypermarket
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
@Table(name = "supermarket")
class Supermarket(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supermarket_id_seq")
    @SequenceGenerator(name = "supermarket_id_seq", sequenceName = "supermarket_id_seq", allocationSize = 1)
    val id: Long = 0,

    @NotNull
    @Enumerated(EnumType.STRING)
    var name: Hypermarket = Hypermarket.OTHER,
)


