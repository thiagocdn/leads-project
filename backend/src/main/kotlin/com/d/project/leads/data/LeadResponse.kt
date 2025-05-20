package com.d.project.leads.data

data class LeadResponse(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val quantityRequested: Int,
    val createdAt: String,
    val updatedAt: String,
) {
    companion object {
        fun from(lead: Lead): LeadResponse {
            return LeadResponse(
                id = lead.id.toString(),
                name = lead.name,
                email = lead.email,
                phone = lead.phone,
                quantityRequested = lead.quantityRequested,
                createdAt = lead.createdAt.toString(),
                updatedAt = lead.updatedAt.toString()
            )
        }
    }
}