package com.d.project.leads.validators

import com.d.project.leads.data.NewLeadRequest
import com.d.project.leads.exception.ValidationException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class LeadValidatorTest {
    private val leadValidator = LeadValidator()

    @Test
    fun `when validate with all corrected data, returns success with cleaned data`() {
        val data = NewLeadRequest(
            name = "John Doe",
            phone = "+5511999999999",
            email = "john@doe.com",
            referralSource = "OTHERS",
            referralOthers = "Familiar",
        )

        val result = leadValidator.validate(data)
        assert(result.isSuccess)

        val cleanedData = result.getOrThrow()
        assertThat(cleanedData.name).isEqualTo("John Doe")
        assertThat(cleanedData.phone).isEqualTo("5511999999999")
    }

    @Test
    fun `when validate with blank name, returns ValidationException with correct messages`() {
        val data = NewLeadRequest(
            name = "",
            phone = "+5511999999999",
            email = "john@doe.com",
            referralSource = "OTHERS",
            referralOthers = "Familiar",
        )

        val exception = leadValidator.validate(data).exceptionOrNull() as ValidationException

        assertThat(exception.message).isEqualTo("Por favor, verifique os erros.")
        assertThat(exception.errors).hasSize(1)
        assertThat(exception.errors[0].field).isEqualTo("name")
        assertThat(exception.errors[0].message).isEqualTo("Nome com pelo menos 3 caracteres é obrigatório.")
    }

    @Test
    fun `when validate with name with less than 3 chars, returns ValidationException with correct messages`() {
        val data = NewLeadRequest(
            name = "a",
            phone = "+5511999999999",
            email = "john@doe.com",
            referralSource = "OTHERS",
            referralOthers = "Familiar",
        )

        val exception = leadValidator.validate(data).exceptionOrNull() as ValidationException

        assertThat(exception.message).isEqualTo("Por favor, verifique os erros.")
        assertThat(exception.errors).hasSize(1)
        assertThat(exception.errors[0].field).isEqualTo("name")
        assertThat(exception.errors[0].message).isEqualTo("Nome com pelo menos 3 caracteres é obrigatório.")
    }

    @Test
    fun `when validate with blank phone, returns ValidationException with correct messages`() {
        val data = NewLeadRequest(
            name = "John Doe",
            phone = "",
            email = "john@doe.com",
            referralSource = "OTHERS",
            referralOthers = "Familiar",
        )

        val exception = leadValidator.validate(data).exceptionOrNull() as ValidationException

        assertThat(exception.message).isEqualTo("Por favor, verifique os erros.")
        assertThat(exception.errors).hasSize(1)
        assertThat(exception.errors[0].field).isEqualTo("phone")
        assertThat(exception.errors[0].message).isEqualTo("Telefone é obrigatório.")
    }

    @Test
    fun `when validate with invalid phone, returns ValidationException with correct messages`() {
        val data = NewLeadRequest(
            name = "John Doe",
            phone = "+51",
            email = "john@doe.com",
            referralSource = "OTHERS",
            referralOthers = "Familiar",
        )

        val exception = leadValidator.validate(data).exceptionOrNull() as ValidationException

        assertThat(exception.message).isEqualTo("Por favor, verifique os erros.")
        assertThat(exception.errors).hasSize(1)
        assertThat(exception.errors[0].field).isEqualTo("phone")
        assertThat(exception.errors[0].message).isEqualTo("Telefone inválido.")
    }

    @Test
    fun `when validate with blank email, returns ValidationException with correct messages`() {
        val data = NewLeadRequest(
            name = "John Doe",
            phone = "+5511999999999",
            email = "",
            referralSource = "OTHERS",
            referralOthers = "Familiar",
        )

        val exception = leadValidator.validate(data).exceptionOrNull() as ValidationException

        assertThat(exception.message).isEqualTo("Por favor, verifique os erros.")
        assertThat(exception.errors).hasSize(1)
        assertThat(exception.errors[0].field).isEqualTo("email")
        assertThat(exception.errors[0].message).isEqualTo("Email é obrigatório.")
    }

    @Test
    fun `when validate with invalid email, returns ValidationException with correct messages`() {
        val data = NewLeadRequest(
            name = "John Doe",
            phone = "+5511999999999",
            email = "invalid",
            referralSource = "OTHERS",
            referralOthers = "Familiar",
        )

        val exception = leadValidator.validate(data).exceptionOrNull() as ValidationException

        assertThat(exception.message).isEqualTo("Por favor, verifique os erros.")
        assertThat(exception.errors).hasSize(1)
        assertThat(exception.errors[0].field).isEqualTo("email")
        assertThat(exception.errors[0].message).isEqualTo("Email inválido.")
    }

    @Test
    fun `when validate with blank referralSource, returns ValidationException with correct messages`() {
        val data = NewLeadRequest(
            name = "John Doe",
            phone = "+5511999999999",
            email = "john@doe.com",
            referralSource = "",
        )

        val exception = leadValidator.validate(data).exceptionOrNull() as ValidationException

        assertThat(exception.message).isEqualTo("Por favor, verifique os erros.")
        assertThat(exception.errors).hasSize(1)
        assertThat(exception.errors[0].field).isEqualTo("referralSource")
        assertThat(exception.errors[0].message).isEqualTo("Fonte de referência é obrigatória.")
    }

    @Test
    fun `when validate with invalid referralSource, returns ValidationException with correct messages`() {
        val data = NewLeadRequest(
            name = "John Doe",
            phone = "+5511999999999",
            email = "john@doe.com",
            referralSource = "INVALID",
        )

        val exception = leadValidator.validate(data).exceptionOrNull() as ValidationException

        assertThat(exception.message).isEqualTo("Por favor, verifique os erros.")
        assertThat(exception.errors).hasSize(1)
        assertThat(exception.errors[0].field).isEqualTo("referralSource")
        assertThat(exception.errors[0].message).isEqualTo("Fonte de referência inválida.")
    }

    @Test
    fun `when validate with OTHERS and no referralOthers, returns ValidationException with correct messages`() {
        val data = NewLeadRequest(
            name = "John Doe",
            phone = "+5511999999999",
            email = "john@doe.com",
            referralSource = "OTHERS",
        )

        val exception = leadValidator.validate(data).exceptionOrNull() as ValidationException

        assertThat(exception.message).isEqualTo("Por favor, verifique os erros.")
        assertThat(exception.errors).hasSize(1)
        assertThat(exception.errors[0].field).isEqualTo("referralOthers")
        assertThat(exception.errors[0].message).isEqualTo("Outros é obrigatório quando a fonte de referência é 'OUTROS'.")
    }

}