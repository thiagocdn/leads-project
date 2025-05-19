package com.d.project.leads.config

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Import

@SpringBootTest
@AutoConfigureMockMvc
@EnableCaching
@Import(
    PostgresTestConfiguration::class,
    LocalStackTestConfiguration::class,
)
annotation class IntegrationTest
