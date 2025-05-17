package com.d.project.leads.data

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "leads")
data class Lead (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    val name: String,
    val phone: String,
    val email: String,
    @Enumerated(EnumType.STRING)
    val referralSource: ReferralSource,
    val referralOthers: String? = null,
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
