package com.d.project.leads.rest.data

import com.d.project.leads.exception.ExceptionError

data class RestResponse<T>(
    val message: String,
    val response: T? = null,
    val errors: List<ExceptionError> = listOf()
)
