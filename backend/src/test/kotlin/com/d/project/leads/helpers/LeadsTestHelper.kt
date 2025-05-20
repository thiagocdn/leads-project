package com.d.project.leads.helpers

import com.d.project.leads.data.Lead
import com.d.project.leads.repository.LeadRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class LeadsTestHelper(
    private val leadRepository: LeadRepository
) {
    fun create(
        name: String = UUID.randomUUID().toString(),
        phone: String = UUID.randomUUID().toString(),
        email: String = "${UUID.randomUUID()}@example.com",
        referralSource: Lead.ReferralSource = Lead.ReferralSource.INTERNET,
        referralOthers: String? = null,
        quantityRequested: Int = 1,
        contacted: Boolean = false
    ): Lead = leadRepository.save(
        Lead(
            name = name,
            phone = phone,
            email = email,
            referralSource = referralSource,
            referralOthers = referralOthers,
            quantityRequested = quantityRequested,
            contacted = contacted
        )
    )

    fun cleanUp() {
        leadRepository.deleteAll()
    }
}