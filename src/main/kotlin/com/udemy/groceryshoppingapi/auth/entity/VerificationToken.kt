package com.udemy.groceryshoppingapi.auth.entity

import com.udemy.groceryshoppingapi.user.entity.AppUser
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "verification_token")
class VerificationToken(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verification_token_id_seq")
    @SequenceGenerator(
        name = "verification_token_id_seq",
        sequenceName = "verification_token_id_seq",
        allocationSize = 1
    )
    val id: Long = 0,

    var token: String,

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "app_user_id")
    val appUser: AppUser,

    var expiryDate: Instant
) {
    fun isExpired(): Boolean = expiryDate.isBefore(Instant.now())
}
