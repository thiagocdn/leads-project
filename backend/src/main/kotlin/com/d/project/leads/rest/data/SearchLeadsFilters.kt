package com.d.project.leads.rest.data

import com.d.project.leads.data.Lead
import org.springframework.data.jpa.domain.Specification

data class SearchLeadsFilters (
    val email: String? = null,
    val phone: String? = null,
    val contacted: Boolean? = null,
)

fun SearchLeadsFilters.toSpecification(): Specification<Lead> {
    return Specification.where<Lead>(null)
        .and(email?.let {
            Specification { root, _, cb -> cb.like(cb.lower(root.get("email")), "%${it.lowercase()}%") }
        })
        .and(phone?.let {
            Specification { root, _, cb -> cb.like(cb.lower(root.get("phone")), "%${it.lowercase()}%") }
        })
        .and(contacted?.let {
            Specification { root, _, cb ->
                if (it) cb.isTrue(root.get("contacted")) else cb.isFalse(root.get("contacted"))
            }
        })
}