package com.d.project.leads.data

data class LeadSnsMessage (
    val name: String,
    val email: String,
    val quantityRequest: Int,
) {
    companion object {
        fun from(lead: Lead, quantityRequest: Int): LeadSnsMessage {
            return LeadSnsMessage(
                name = lead.name,
                email = lead.email,
                quantityRequest = quantityRequest
            )
        }
    }
}
