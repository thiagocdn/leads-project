package com.d.project.leads.rest

import com.d.project.leads.config.IntegrationTest
import com.d.project.leads.data.NewLeadRequest
import com.d.project.leads.helpers.LeadsTestHelper
import com.d.project.leads.helpers.ObjectHelper.toJson
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.UUID

@IntegrationTest
class LeadsRestTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var leadsTestHelper: LeadsTestHelper

    @BeforeEach
    fun init() {
        leadsTestHelper.cleanUp()
    }

    @Test
    fun `when create lead without errors, returns success`() {
        val lead = NewLeadRequest(
            name = "John Doe",
            phone = "1234567890",
            email = "john@doe.com",
            referralSource = "INTERNET"
        )

        mockMvc.post("/leads") {
            contentType = MediaType.APPLICATION_JSON
            content = lead.toJson()
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `when try to create lead with invalid data, returns the error`() {
        val lead = NewLeadRequest(
            name = "",
            phone = "1234567890",
            email = "john@doe.com",
            referralSource = "INTERNET"
        )

        mockMvc.post("/leads") {
            contentType = MediaType.APPLICATION_JSON
            content = lead.toJson()
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") { value("Por favor, verifique os erros.") }
            jsonPath("$.errors[0].field") { value("name") }
            jsonPath("$.errors[0].message") {
                value("Nome com pelo menos 3 caracteres é obrigatório.")
            }
        }
    }

    @Test
    fun `when find not contacted, returns not contacted leads`() {
        val leadNotContacted = leadsTestHelper.create()
        leadsTestHelper.create(contacted = true)

        mockMvc.get("/leads") {
            param("page", "0")
            param("size", "10")
            param("contacted", "false")
        }.andExpect {
            status { isOk() }
            jsonPath("$.message") { value("Leads retrieved successfully.") }
            jsonPath("$.response.totalPages") { value(1) }
            jsonPath("$.response.totalElements") { value(1) }
            jsonPath("$.response.content[0].id") { value(leadNotContacted.id.toString()) }
        }
    }

    @Test
    fun `when find leads by e-mail, returns all leads with the given email`() {
        val email = "${UUID.randomUUID()}@doe.com"
        leadsTestHelper.create(email = email)
        leadsTestHelper.create(email = email)
        leadsTestHelper.create(email = "${UUID.randomUUID()}@doe.com")

        mockMvc.get("/leads") {
            param("email", email)
        }.andExpect {
            status { isOk() }
            jsonPath("$.message") { value("Leads retrieved successfully.") }
            jsonPath("$.response.totalPages") { value(1) }
            jsonPath("$.response.totalElements") { value(2) }
            jsonPath("$.response.content[0].email") { value(email) }
            jsonPath("$.response.content[1].email") { value(email) }
        }
    }

    @Test
    fun `when find leads by e-mail and not contacted, returns all leads with the given email and not contacted`() {
        val email = "${UUID.randomUUID()}@doe.com"
        leadsTestHelper.create(email = email, contacted = true)
        leadsTestHelper.create(email = email)
        leadsTestHelper.create(email = "${UUID.randomUUID()}@doe.com")

        mockMvc.get("/leads") {
            param("email", email)
            param("contacted", "false")
        }.andExpect {
            status { isOk() }
            jsonPath("$.message") { value("Leads retrieved successfully.") }
            jsonPath("$.response.totalPages") { value(1) }
            jsonPath("$.response.totalElements") { value(1) }
            jsonPath("$.response.content[0].email") { value(email) }
        }
    }

    @Test
    fun `when update lead contact, returns success`() {
        val lead = leadsTestHelper.create()

        mockMvc.post("/leads/${lead.id.toString()}/contacted").andExpect {
            status { isOk() }
            jsonPath("$.message") { value("Lead updated successfully.") }
        }
    }

    @Test
    fun `when find leads by phone, returns all leads with the given email`() {
        val phone = "+511234567890"
        leadsTestHelper.create(phone = phone)
        leadsTestHelper.create(phone = phone)
        leadsTestHelper.create()

        mockMvc.get("/leads") {
            param("phone", phone)
        }.andExpect {
            status { isOk() }
            jsonPath("$.message") { value("Leads retrieved successfully.") }
            jsonPath("$.response.totalPages") { value(1) }
            jsonPath("$.response.totalElements") { value(2) }
            jsonPath("$.response.content[0].phone") { value(phone) }
            jsonPath("$.response.content[1].phone") { value(phone) }
        }
    }

    @Test
    fun `when try to update lead contact with invalid id, returns validation error`() {
        mockMvc.post("/leads/invalid/contacted").andExpect {
            status { isBadRequest() }
            jsonPath("$.message") { value("Id 'invalid' not valid.") }
        }
    }

    @Test
    fun `when try to update lead contact with non-existent id, returns not found error`() {
        val nonExistentId = UUID.randomUUID().toString()
        mockMvc.post("/leads/$nonExistentId/contacted") {
            param("page", "0")
            param("size", "5")
        }.andExpect {
            status { isNotFound() }
            jsonPath("$.message") { value("Lead with id $nonExistentId not found.") }
        }
    }

    @Test
    fun `when contact by e-mail, returns success`() {
        val email = "${UUID.randomUUID()}@doe.com"
        leadsTestHelper.create(email = email)

        mockMvc.post("/leads/email-contacted/$email").andExpect {
            status { isOk() }
            jsonPath("$.message") { value("All leads with e-mail $email were contacted.") }
        }
    }

    @Test
    fun `when try contact by e-mail with non existent contacting email, returns Not Found error`() {
        val email = "${UUID.randomUUID()}@doe.com"

        mockMvc.post("/leads/email-contacted/$email").andExpect {
            status { isNotFound() }
            jsonPath("$.message") { value("No leads found with email $email.") }
        }
    }

    @Test
    fun `when contact by phone, returns success`() {
        val phone = UUID.randomUUID().toString()
        leadsTestHelper.create(phone = phone)

        mockMvc.post("/leads/phone-contacted/$phone").andExpect {
            status { isOk() }
            jsonPath("$.message") { value("All leads with phone $phone were contacted.") }
        }
    }

    @Test
    fun `when try contact by phone with non existent contacting email, returns Not Found error`() {
        val phone = UUID.randomUUID().toString()

        mockMvc.post("/leads/phone-contacted/$phone").andExpect {
            status { isNotFound() }
            jsonPath("$.message") { value("No leads found with phone $phone.") }
        }
    }
}