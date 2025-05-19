package com.d.project.leads.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.AmazonSNSClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SnsConfig(
    private val snsProperties: SnsProperties,
) {

    @Value("\${cloud.aws.credentials.access-key}")
    lateinit var accessKey: String

    @Value("\${cloud.aws.credentials.secret-key}")
    lateinit var secretKey: String

    @Value("\${cloud.aws.region.static}")
    lateinit var region: String

    @Value("\${cloud.aws.sns.endpoint}")
    lateinit var snsEndpoint: String

    @Bean
    fun amazonSNS(): AmazonSNS {
        val sns = AmazonSNSClientBuilder.standard()
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(snsEndpoint, region)
            )
            .withCredentials(
                AWSStaticCredentialsProvider(BasicAWSCredentials(accessKey, secretKey))
            )
            .build()

        val result = sns.createTopic(snsProperties.leadsTopicName)
        snsProperties.topicArn = result.topicArn

        return sns
    }
}