package org.ein.erste.iot.hivemq.extension;

import com.hivemq.extension.sdk.api.interceptor.publish.PublishInboundInterceptor;
import com.hivemq.extension.sdk.api.interceptor.publish.parameter.PublishInboundInput;
import com.hivemq.extension.sdk.api.interceptor.publish.parameter.PublishInboundOutput;
import com.hivemq.extension.sdk.api.packets.publish.ModifiablePublishPacket;
import com.hivemq.extension.sdk.api.services.Services;
import com.hivemq.extension.sdk.api.services.builder.Builders;
import com.hivemq.extension.sdk.api.services.publish.PublishService;

import java.net.Socket;

import static org.ein.erste.iot.hivemq.extension.MyExtensionMain.CONFIG;

//collects all data for analysis
public class PublishInterceptor implements PublishInboundInterceptor {
    private Socket socket;
    @Override
    public void onInboundPublish(final PublishInboundInput publishInboundInput,
                                 final PublishInboundOutput publishInboundOutput) {
        final ModifiablePublishPacket publishPacket = publishInboundOutput.getPublishPacket();
        final PublishService publishService = Services.publishService();

        // Duplicate the message only if it's not already to "traffic/data"
        if (!publishPacket.getTopic().equals(CONFIG.logstashTopic()) && publishPacket.getPayload().isPresent()) {
            publishService.publish(Builders.publish()
                                           .topic(CONFIG.logstashTopic())
                                           .payload(publishPacket.getPayload().get())
                                           .qos(publishPacket.getQos())
                                           .retain(publishPacket.getRetain())
                                           .build());
        }
    }
}