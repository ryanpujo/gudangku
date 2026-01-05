package com.rprojects.gudangku.credential.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant


enum class AccountStatus {
    ACTIVE,
    DISABLED,
    LOCKED
}

@Entity
data class Credential(

    @Id
    val username: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false, unique = true)
    val phoneNumber: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val accountStatus: AccountStatus = AccountStatus.DISABLED,

    @CreationTimestamp
    var createdAt: Instant? = null,

    @UpdateTimestamp
    var updatedAt: Instant? = null
) {
    protected constructor(): this(
        username = "",
        password = "",
        email = "",
        phoneNumber = "",
        accountStatus = AccountStatus.DISABLED,
        createdAt = null,
        updatedAt = null
    )
}