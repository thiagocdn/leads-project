package com.d.project.leads.service

import com.d.project.leads.validators.LeadValidator
import com.d.project.leads.data.Lead
import com.d.project.leads.data.LeadSnsMessage
import com.d.project.leads.data.NewLeadRequest
import com.d.project.leads.exception.NotFoundException
import com.d.project.leads.repository.LeadRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class LeadsService(
    private val leadRepository: LeadRepository,
    private val leadValidator: LeadValidator,
    private val snsPublisher: SnsPublisher,
    private val objectMapper: ObjectMapper
) {
    fun createLead(request: NewLeadRequest): Result<Lead> = leadValidator.validate(request).map {
        val savedLead = leadRepository.save(request.toLead())
        snsPublishHandler(savedLead)
        return Result.success(savedLead)
    }

    private fun snsPublishHandler(lead: Lead) {
        val quantityRequest = leadRepository.countByEmail(lead.email)
        val leadSnsMessage = LeadSnsMessage.from(lead, quantityRequest.toInt())
        snsPublisher.publishMessage(objectMapper.writeValueAsString(leadSnsMessage))
    }

    fun getLead(id: String): Result<Lead> = runCatching {
        leadRepository.findById(id).orElseThrow { NotFoundException("Lead not found.") }
    }
}
