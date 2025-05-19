package com.d.project.leads.rest

import com.d.project.leads.data.LeadResponse
import com.d.project.leads.data.NewLeadRequest
import com.d.project.leads.rest.data.PaginatedResponse
import com.d.project.leads.rest.data.RestResponse
import com.d.project.leads.service.LeadsService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/leads")
class LeadsRest (
    private val leadsService: LeadsService,
) {
    @PostMapping
    fun createLead(
        @RequestBody request: NewLeadRequest
    ): RestResponse<Unit> {
        leadsService.createLead(request).getOrThrow()
        return RestResponse(
            message = "Lead created successfully.",
            response = Unit
        )
    }

    @GetMapping("/not-contacted")
    fun findNotContacted(
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 20
    ): RestResponse<PaginatedResponse<LeadResponse>> {
        val leads = leadsService.listLeadsNotContactedPaginated(page, size).getOrThrow()

        return RestResponse(
            message = "Leads retrieved successfully.",
            response = leads
        )
    }

    @PostMapping("/{id}/contacted")
    fun updateLeadContacted(
        @PathVariable id: String,
    ): RestResponse<Unit> {
        leadsService.updateLeadContacted(id).getOrThrow()
        return RestResponse(
            message = "Lead updated successfully.",
            response = Unit
        )
    }

    @GetMapping
    fun findByEmail(
        @RequestParam email: String
    ): RestResponse<List<LeadResponse>> {
        val leads = leadsService.findByEmail(email).getOrThrow().map { LeadResponse.from(it) }
        return RestResponse(
            message = "Leads retrieved successfully.",
            response = leads
        )
    }

    @PostMapping("/email-contacted")
    fun updateLeadsContactedByEmail(
        @RequestParam email: String,
    ): RestResponse<Unit> {
        leadsService.updateLeadsContactedByEmail(email).getOrThrow()
        return RestResponse(
            message = "All leads with e-mail $email were contacted.",
            response = Unit
        )
    }
}
