package com.d.project.leads.data

data class LeadResponse(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val createdAt: String,
    val updatedAt: String,
) {
    companion object {
        fun from(lead: Lead): LeadResponse {
            return LeadResponse(
                id = lead.id ?: "",
                name = lead.name,
                email = lead.email,
                phone = lead.phone,
                createdAt = lead.createdAt.toString(),
                updatedAt = lead.updatedAt.toString()
            )
        }
    }
}