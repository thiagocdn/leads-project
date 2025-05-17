package com.d.project.leads.exception

class NotFoundException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : Exception(null, cause)
