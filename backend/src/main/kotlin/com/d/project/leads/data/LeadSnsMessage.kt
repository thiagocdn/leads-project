package com.d.project.leads.data

data class LeadSnsMessage (
    val name: String,
    val email: String,
    val quantityRequested: Int,
) {
    companion object {
        fun from(lead: Lead): LeadSnsMessage {
            return LeadSnsMessage(
                name = lead.name,
                email = lead.email,
                quantityRequested = lead.quantityRequested
            )
        }
    }
}
