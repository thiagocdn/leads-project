package com.d.project.leads.helpers

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

object ObjectHelper {
    fun Any.toJson(): String {
        return jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .writeValueAsString(this)
    }
}
