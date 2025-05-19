package com.d.project.leads.validators

import com.d.project.leads.data.Lead
import com.d.project.leads.data.NewLeadRequest
import com.d.project.leads.exception.ExceptionError
import com.d.project.leads.exception.ValidationException
import org.springframework.stereotype.Component

@Component
class LeadValidator {
    fun validate(request: NewLeadRequest): Result<NewLeadRequest> {
        val errors = mutableListOf<ExceptionError>()
        if (request.name.isBlank() || request.name.length < 3) {
            errors.add(ExceptionError("name", "Nome com pelo menos 3 caracteres é obrigatório."))
        }

        val cleanedPhone = request.phone.replace(Regex("\\D"), "")
        if (request.phone.isBlank()) {
            errors.add(ExceptionError("phone", "Telefone é obrigatório."))
        } else if (!cleanedPhone.matches(Regex("^\\+?[0-9]{10,15}\$"))) {
            errors.add(ExceptionError("phone", "Telefone inválido."))
        }

        if (request.email.isBlank()) {
            errors.add(ExceptionError("email", "Email é obrigatório."))
        } else if (!request.email.matches(Regex("^[\\w-.]+@[\\w-]+\\.[a-z]{2,4}$"))) {
            errors.add(ExceptionError("email", "Email inválido."))
        }

        if (request.referralSource.isNullOrBlank()) {
            errors.add(ExceptionError("referralSource", "Fonte de referência é obrigatória."))
        } else if (!Lead.ReferralSource.entries.any { it.name.equals(request.referralSource, ignoreCase = true) }) {
            errors.add(ExceptionError("referralSource", "Fonte de referência inválida."))
        } else if (request.referralSource == Lead.ReferralSource.OTHERS.name && request.referralOthers.isNullOrBlank()) {
            errors.add(
                ExceptionError(
                    "referralOthers",
                    "Outros é obrigatório quando a fonte de referência é 'OUTROS'."
                )
            )
        }

        return if (errors.isNotEmpty()) {
            Result.failure(ValidationException(message = "Por favor, verifique os erros.", errors = errors))
        } else {
            Result.success(request.copy(phone = request.phone.replace(Regex("\\D"), "")))
        }
    }
}
