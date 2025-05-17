package com.d.project.leads.rest

import com.d.project.leads.data.LeadResponse
import com.d.project.leads.data.NewLeadRequest
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

    @GetMapping("/{leadId}")
    fun getLeads(
        @PathVariable leadId: String
    ): RestResponse<LeadResponse> {
        val lead = leadsService.getLead(leadId).getOrThrow()
        return RestResponse(
            message = "Lead retrieved successfully.",
            response = LeadResponse.from(lead)
        )
    }
}
