package com.d.project.leads.rest

import com.d.project.leads.data.LeadResponse
import com.d.project.leads.data.NewLeadRequest
import com.d.project.leads.rest.data.PaginatedResponse
import com.d.project.leads.rest.data.RestResponse
import com.d.project.leads.rest.data.SearchLeadsFilters
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

    @GetMapping
    fun searchLeads(
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        @ModelAttribute filters: SearchLeadsFilters
    ): RestResponse<PaginatedResponse<LeadResponse>> {
        val leads = leadsService.searchLeadsPaginated(
            page = page,
            size = size,
            filters = filters
        ).getOrThrow()

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

    @PostMapping("/email-contacted/{leadEmail}")
    fun updateLeadsContactedByEmail(
        @PathVariable leadEmail: String
    ): RestResponse<Unit> {
        leadsService.updateLeadsContactedByEmail(leadEmail).getOrThrow()
        return RestResponse(
            message = "All leads with e-mail $leadEmail were contacted.",
            response = Unit
        )
    }

    @PostMapping("/phone-contacted/{leadPhone}")
    fun updateLeadsContactedByPhone(
        @PathVariable leadPhone: String
    ): RestResponse<Unit> {
        leadsService.updateLeadsContactedByPhone(leadPhone).getOrThrow()
        return RestResponse(
            message = "All leads with phone $leadPhone were contacted.",
            response = Unit
        )
    }
}
