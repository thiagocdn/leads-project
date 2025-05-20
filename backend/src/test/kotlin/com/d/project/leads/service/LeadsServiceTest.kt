package com.d.project.leads.service

import com.d.project.leads.config.IntegrationTest
import com.d.project.leads.data.LeadSnsMessage
import com.d.project.leads.data.NewLeadRequest
import com.d.project.leads.exception.NotFoundException
import com.d.project.leads.exception.ValidationException
import com.d.project.leads.helpers.LeadsTestHelper
import com.d.project.leads.rest.data.SearchLeadsFilters
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.UUID

@IntegrationTest
class LeadsServiceTest {
    @Autowired
    private lateinit var leadsService: LeadsService

    @Autowired
    private lateinit var leadsTestHelper: LeadsTestHelper

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var snsPublisher: SnsPublisher

    @BeforeEach
    fun init() {
        leadsTestHelper.cleanUp()
    }

    @Test
    fun `when create lead with all correct data, publishes sns message and returns the lead created`() {
        val request = NewLeadRequest(
            name = "John Doe",
            phone = "+1234567890",
            email = "john@doe.com",
            referralSource = "INTERNET"
        )

        val result = leadsService.createLead(request).getOrThrow()
        val messageToPublish = objectMapper.writeValueAsString(LeadSnsMessage.from(result))

        verify(snsPublisher).publishMessage(messageToPublish)
        assertThat(result.name).isEqualTo("John Doe")
        assertThat(result.phone).isEqualTo("1234567890")
        assertThat(result.email).isEqualTo("john@doe.com")
        assertThat(result.quantityRequested).isEqualTo(1)
    }

    @Test
    fun `when create a lead two times, returns the lead with requestedQuantity as 2`() {
        val request = NewLeadRequest(
            name = "John Doe",
            phone = "+1234567890",
            email = "john@doe.com",
            referralSource = "INTERNET"
        )

        leadsService.createLead(request).getOrThrow()
        val result = leadsService.createLead(request).getOrThrow()

        assertThat(result.name).isEqualTo("John Doe")
        assertThat(result.phone).isEqualTo("1234567890")
        assertThat(result.email).isEqualTo("john@doe.com")
        assertThat(result.quantityRequested).isEqualTo(2)
    }

    @Test
    fun `when try to create a lead with invalid data, returns error`() {
        val request = NewLeadRequest(
            name = "John Doe",
            phone = "+1234567890",
            email = "john@doe.com",
            referralSource = "OTHERS"
        )

        val exception = leadsService.createLead(request).exceptionOrNull() as ValidationException

        assertThat(exception.message).isEqualTo("Por favor, verifique os erros.")
        assertThat(exception.errors[0].message).isEqualTo(
            "Outros é obrigatório quando a fonte de referência é 'OUTROS'."
        )
        assertThat(exception.errors[0].field).isEqualTo("referralOthers")
    }

    @Test
    fun `when search for leads with query params, returns the correct result`() {
        val email = "${UUID.randomUUID()}@example.com"
        leadsTestHelper.create(email = "${UUID.randomUUID()}@example2.com")
        leadsTestHelper.create(email = email)
        leadsTestHelper.create(email = email, phone = "1234567890")
        leadsTestHelper.create(email = email, phone = "3216547895")

        val filters = SearchLeadsFilters(email = email)

        val result = leadsService.searchLeadsPaginated(0, 10, filters).getOrThrow()

        assertThat(result.content.size).isEqualTo(3)
        assertThat(result.content[0].email).isEqualTo(email)
    }

    @Test
    fun `when get a lead with a valid lead, returns the lead`() {
        val lead = leadsTestHelper.create()

        val result = leadsService.getLead(lead.id.toString()).getOrThrow()

        assertThat(result.id).isEqualTo(lead.id)
        assertThat(result.name).isEqualTo(lead.name)
    }

    @Test
    fun `when get a lead which does not exists, returns NotFoundException`() {
        val leadId = UUID.randomUUID().toString()
        val exception = leadsService.getLead(leadId).exceptionOrNull() as NotFoundException

        assertThat(exception.message).isEqualTo("Lead with id $leadId not found.")
    }

    @Test
    fun `when get a lead with invalid UUID, returns ValidationException`() {
        val leadId = "invalid-uuid"
        val exception = leadsService.getLead(leadId).exceptionOrNull() as ValidationException

        assertThat(exception.message).isEqualTo("Id '$leadId' not valid.")
    }

    @Test
    fun `when update lead contacted, returns the lead updated`() {
        val lead = leadsTestHelper.create(contacted = false)

        val result = leadsService.updateLeadContacted(lead.id.toString()).getOrThrow()

        assertThat(result.id).isEqualTo(lead.id)
        assertThat(result.contacted).isTrue()
    }

    @Test
    fun `when update lead contacted with invalid UUID, returns ValidationException`() {
        val leadId = "invalid-uuid"
        val exception = leadsService.updateLeadContacted(leadId).exceptionOrNull() as ValidationException

        assertThat(exception.message).isEqualTo("Id '$leadId' not valid.")
    }

    @Test
    fun `when update lead contacted with non-existing lead, returns NotFoundException`() {
        val leadId = UUID.randomUUID().toString()
        val exception = leadsService.updateLeadContacted(leadId).exceptionOrNull() as NotFoundException

        assertThat(exception.message).isEqualTo("Lead with id $leadId not found.")
    }

    @Test
    fun `when update leads contacted by email, returns success and update the leads`() {
        val email = "${UUID.randomUUID()}@example.com"
        val lead1 = leadsTestHelper.create(email = email, contacted = false)
        val lead2 = leadsTestHelper.create(email = email, contacted = false)

        leadsService.updateLeadsContactedByEmail(email).getOrThrow()

        val lead1Updated = leadsService.getLead(lead1.id.toString()).getOrThrow()
        val lead2Updated = leadsService.getLead(lead2.id.toString()).getOrThrow()

        assertThat(lead1Updated.contacted).isTrue()
        assertThat(lead2Updated.contacted).isTrue()
    }

    @Test
    fun `when update leads contacted by email with non-existing email, returns NotFoundException`() {
        val email = "${UUID.randomUUID()}@example.com"
        val exception = leadsService.updateLeadsContactedByEmail(email).exceptionOrNull() as NotFoundException

        assertThat(exception.message).isEqualTo("No leads found with email $email.")
    }

    @Test
    fun `when update leads contacted by phone, returns success and update the leads`() {
        val phone = "1234567890"
        val lead1 = leadsTestHelper.create(phone = phone, contacted = false)
        val lead2 = leadsTestHelper.create(phone = phone, contacted = false)

        leadsService.updateLeadsContactedByPhone(phone).getOrThrow()

        val lead1Updated = leadsService.getLead(lead1.id.toString()).getOrThrow()
        val lead2Updated = leadsService.getLead(lead2.id.toString()).getOrThrow()

        assertThat(lead1Updated.contacted).isTrue()
        assertThat(lead2Updated.contacted).isTrue()
    }

    @Test
    fun `when update leads contacted by phone with non-existing phone, returns NotFoundException`() {
        val phone = "1234567890"
        val exception = leadsService.updateLeadsContactedByPhone(phone).exceptionOrNull() as NotFoundException

        assertThat(exception.message).isEqualTo("No leads found with phone $phone.")
    }
}