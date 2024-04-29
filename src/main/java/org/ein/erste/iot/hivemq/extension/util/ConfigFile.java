package org.ein.erste.iot.hivemq.extension.util;

public record ConfigFile(String url, String apiKey, String logstashLogin, String logstashPassword) {
    public static final String LOGSTASH_TOPIC = "traffic/data";
}
