package com.d.project.leads.data

data class NewLeadRequest (
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val referralSource: String? = null,
    val referralOthers: String? = null,
) {
    fun toLead(): Lead {
        return Lead(
            name = this.name,
            phone = this.phone,
            email = this.email,
            referralSource = Lead.ReferralSource.valueOf(this.referralSource!!.uppercase()),
            referralOthers = this.referralOthers,
        )
    }
}
