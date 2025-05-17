package com.d.project.leads.service

import com.d.project.leads.validators.LeadValidator
import com.d.project.leads.data.Lead
import com.d.project.leads.data.NewLeadRequest
import com.d.project.leads.exception.NotFoundException
import com.d.project.leads.repository.LeadRepository
import org.springframework.stereotype.Service

@Service
class LeadsService(
    private val leadRepository: LeadRepository,
    private val leadValidator: LeadValidator,
) {
    fun createLead(request: NewLeadRequest): Result<Lead> = leadValidator.validate(request).map {
        val savedLead = leadRepository.save(request.toLead())
        return Result.success(savedLead)
    }

    fun getLead(id: String): Result<Lead> = runCatching {
        leadRepository.findById(id).orElseThrow { NotFoundException("Lead not found.") }
    }
}
