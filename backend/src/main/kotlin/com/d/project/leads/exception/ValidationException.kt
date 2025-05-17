package com.d.project.leads.exception

class ValidationException(
    override val message: String? = null,
    val errors: List<ExceptionError> = emptyList(),
    override val cause: Throwable? = null
) : Exception(null, cause)
