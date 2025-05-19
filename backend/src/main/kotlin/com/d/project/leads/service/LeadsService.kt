package com.d.project.leads.service

import com.d.project.leads.validators.LeadValidator
import com.d.project.leads.data.Lead
import com.d.project.leads.data.LeadResponse
import com.d.project.leads.data.LeadSnsMessage
import com.d.project.leads.data.NewLeadRequest
import com.d.project.leads.exception.NotFoundException
import com.d.project.leads.repository.LeadRepository
import com.d.project.leads.rest.data.PaginatedResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class LeadsService(
    private val leadRepository: LeadRepository,
    private val leadValidator: LeadValidator,
    private val snsPublisher: SnsPublisher,
    private val objectMapper: ObjectMapper
) {
    fun createLead(request: NewLeadRequest): Result<Lead> = leadValidator.validate(request).map { cleanedRequest ->
        val lead = leadRepository.findByEmailIgnoreCaseAndNameIgnoreCaseAndPhoneIgnoreCaseAndContactedFalse(
            cleanedRequest.email,
            cleanedRequest.name,
            cleanedRequest.phone
        )?.let {
            leadRepository.save(it.copy(
                quantityRequested = it.quantityRequested.plus(1),
                updatedAt = LocalDateTime.now(),
            ))
        } ?: leadRepository.save(cleanedRequest.toLead())
        snsPublishHandler(lead)

        return Result.success(lead)
    }

    private fun snsPublishHandler(lead: Lead) {
        val leadSnsMessage = LeadSnsMessage.from(lead)
        snsPublisher.publishMessage(objectMapper.writeValueAsString(leadSnsMessage))
    }

    fun getLead(id: String): Result<Lead> = runCatching {
        leadRepository.findById(id).orElseThrow { NotFoundException("Lead not found.") }
    }

    fun listLeadsNotContactedPaginated(page: Int, size: Int): Result<PaginatedResponse<LeadResponse>> = runCatching {
        val sort = Sort.by(
            Sort.Order.desc("quantityRequested"),
            Sort.Order.asc("createdAt")
        )
        val pageable = PageRequest.of(page, size, sort)
        val leadsPage = leadRepository.findByContactedFalse(pageable)

        PaginatedResponse(
            content = leadsPage.content.map { LeadResponse.from(it) },
            page = leadsPage.number,
            size = leadsPage.size,
            totalPages = leadsPage.totalPages,
            totalElements = leadsPage.totalElements,
            hasNext = leadsPage.hasNext(),
            hasPrevious = leadsPage.hasPrevious()
        )
    }

    fun findByEmail(email: String): Result<List<Lead>> = runCatching {
        leadRepository.findByEmail(email)
    }

    fun updateLeadContacted(leadId: String): Result<Lead> = getLead(leadId).map { lead ->
        leadRepository.save(lead.copy(contacted = true))
    }

    fun updateLeadsContactedByEmail(email: String): Result<Unit> = findByEmail(email).map { leads ->
        leads.forEach { lead ->
            leadRepository.save(lead.copy(contacted = true))
        }
    }
}
