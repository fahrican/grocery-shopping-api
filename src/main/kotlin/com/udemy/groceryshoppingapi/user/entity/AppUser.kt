package com.udemy.groceryshoppingapi.user.entity

import com.udemy.groceryshoppingapi.auth.Role
import com.udemy.groceryshoppingapi.auth.entity.VerificationToken
import com.udemy.groceryshoppingapi.retail.entity.ShoppingList
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "app_user")
class AppUser(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_id_seq")
    @SequenceGenerator(name = "app_user_id_seq", sequenceName = "app_user_id_seq", allocationSize = 1)
    val id: Long = 0,

    @NotBlank
    var firstName: String = "",

    @NotBlank
    var lastName: String = "",

    @NotBlank
    @Column(unique = true, nullable = false)
    var email: String = "",

    @NotBlank
    @Column(unique = true, nullable = false)
    var appUsername: String = "",

    @NotBlank
    var appPassword: String = "",

    @NotNull
    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER,

    var isVerified: Boolean = false,

    @OneToOne(mappedBy = "appUser", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, optional = true)
    private val verificationToken: VerificationToken? = null,

    @OneToMany(mappedBy = "appUser", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    private val shoppingLists: List<ShoppingList>? = null
) : UserDetails {

    override fun getAuthorities(): List<GrantedAuthority> = listOf(SimpleGrantedAuthority(role.name))

    override fun getPassword() = appPassword

    override fun getUsername() = appUsername

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = isVerified
}