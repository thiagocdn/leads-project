package com.d.project.leads.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "aws.sns")
class SnsProperties {
    lateinit var leadsTopicName: String
    var topicArn: String? = null
}
