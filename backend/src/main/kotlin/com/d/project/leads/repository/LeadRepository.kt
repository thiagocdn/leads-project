package com.d.project.leads.repository

import com.d.project.leads.data.Lead
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LeadRepository: JpaRepository<Lead, String> {
    fun findByEmail(email: String): List<Lead>
    fun findByEmailAndContactedFalse(email: String): List<Lead>
    fun findByPhoneAndContactedFalse(phone: String): List<Lead>
    fun findByContactedFalse(pageable: Pageable): Page<Lead>
    fun findByEmailIgnoreCaseAndNameIgnoreCaseAndPhoneIgnoreCaseAndContactedFalse(
        email: String,
        name: String,
        phone: String): Lead?
}
