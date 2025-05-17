package com.d.project.leads.config

import org.springframework.boot.test.context.TestConfiguration
import org.testcontainers.containers.PostgreSQLContainer

@TestConfiguration
class PostgresTestConfiguration {
    companion object {
        init {
            val postgres = PostgreSQLContainer<Nothing>("postgres:15").apply {
                withDatabaseName("testdb")
                withUsername("test")
                withPassword("test")
                start()
            }

            System.setProperty("spring.datasource.url", postgres.jdbcUrl)
            System.setProperty("spring.datasource.username", postgres.username)
            System.setProperty("spring.datasource.password", postgres.password)
            System.setProperty("spring.datasource.driver-class-name", "org.postgresql.Driver")
        }
    }
}