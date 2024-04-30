package org.ein.erste.iot.hivemq.extension.util;

public record ConfigFile(String authServerUrl,
                         String authServerApiKey,
                         String logstashTopic,
                         String logstashReaderId,
                         String logstashReaderPassword) {
}
