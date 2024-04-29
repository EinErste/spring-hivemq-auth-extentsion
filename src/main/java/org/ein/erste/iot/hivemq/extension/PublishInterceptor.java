package org.ein.erste.iot.hivemq.extension;

import com.hivemq.extension.sdk.api.interceptor.publish.PublishInboundInterceptor;
import com.hivemq.extension.sdk.api.interceptor.publish.parameter.PublishInboundInput;
import com.hivemq.extension.sdk.api.interceptor.publish.parameter.PublishInboundOutput;
import com.hivemq.extension.sdk.api.packets.publish.ModifiablePublishPacket;
import com.hivemq.extension.sdk.api.services.Services;
import com.hivemq.extension.sdk.api.services.builder.Builders;
import com.hivemq.extension.sdk.api.services.publish.Publish;
import com.hivemq.extension.sdk.api.services.publish.PublishService;
import org.ein.erste.iot.hivemq.extension.util.ConfigFile;

//collects all data for analysis
public class PublishInterceptor implements PublishInboundInterceptor {
    @Override
    public void onInboundPublish(final PublishInboundInput publishInboundInput,
                                 final PublishInboundOutput publishInboundOutput) {
        final ModifiablePublishPacket publishPacket = publishInboundOutput.getPublishPacket();
        final PublishService publishService = Services.publishService();

        // Duplicate the message only if it's not already to "traffic/data"
        if (!publishPacket.getTopic().equals(ConfigFile.LOGSTASH_TOPIC) && publishPacket.getPayload().isPresent()) {
            publishService.publish(Builders.publish()
                    .topic(ConfigFile.LOGSTASH_TOPIC)
                    .payload(publishPacket.getPayload().get())
                    .qos(publishPacket.getQos())
                    .retain(publishPacket.getRetain())
                    .build());
        }
    }
}