package com.d.project.leads.data

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "leads")
data class Lead (
    @Id
    @GeneratedValue
    val id: UUID? = null,
    val name: String,
    val phone: String,
    val email: String,
    @Enumerated(EnumType.STRING)
    val referralSource: ReferralSource,
    val referralOthers: String? = null,
    val quantityRequested: Int = 1,
    val contacted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
) {
    enum class ReferralSource {
        INTERNET,
        REFERRAL,
        EVENTS,
        OTHERS
    }
}
