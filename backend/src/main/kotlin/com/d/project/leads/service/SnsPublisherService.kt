package com.d.project.leads.service

import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.PublishRequest
import com.d.project.leads.config.SnsProperties
import org.springframework.stereotype.Service

@Service
class SnsPublisher(
    private val amazonSNS: AmazonSNS,
    private val snsProperties: SnsProperties
) {
    fun publishMessage(message: String) {
        val request = PublishRequest()
            .withTopicArn(snsProperties.topicArn)
            .withMessage(message)
        amazonSNS.publish(request)
    }
}