package com.d.project.leads.repository

import com.d.project.leads.data.Lead
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LeadRepository: JpaRepository<Lead, String> {
    fun findByEmail(email: String): Lead?
    fun countByEmail(email: String): Long
}
