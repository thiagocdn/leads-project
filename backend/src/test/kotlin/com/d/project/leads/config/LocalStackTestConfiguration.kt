package com.d.project.leads.config

import org.springframework.boot.test.context.TestConfiguration
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration
class LocalStackTestConfiguration {
    companion object {
        private val localstack: LocalStackContainer = LocalStackContainer(
            DockerImageName.parse("localstack/localstack:2.3")
        ).withServices(LocalStackContainer.Service.SNS)

        init {
            localstack.start()

            val endpoint = localstack.getEndpointOverride(LocalStackContainer.Service.SNS).toString()
            val accessKey = localstack.accessKey
            val secretKey = localstack.secretKey
            val region = localstack.region

            System.setProperty("cloud.aws.sns.endpoint", endpoint)
            System.setProperty("cloud.aws.credentials.access-key", accessKey)
            System.setProperty("cloud.aws.credentials.secret-key", secretKey)
            System.setProperty("cloud.aws.region.static", region)
        }
    }
}
