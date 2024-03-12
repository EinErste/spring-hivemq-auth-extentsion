package org.ein.erste.iot.hivemq.extension;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.auth.PublishAuthorizer;
import com.hivemq.extension.sdk.api.auth.parameter.PublishAuthorizerInput;
import com.hivemq.extension.sdk.api.auth.parameter.PublishAuthorizerOutput;
import com.hivemq.extension.sdk.api.packets.publish.PublishPacket;

public class CustomPublishAuthorizer implements PublishAuthorizer {
    @Override
    public void authorizePublish(@NotNull PublishAuthorizerInput input, @NotNull PublishAuthorizerOutput output) {
        PublishPacket publishPacket = input.getPublishPacket();
        String topicName = publishPacket.getTopic();
        boolean isAuthorized = checkAuthorization(input.getClientInformation().getClientId(), topicName);

        if (isAuthorized) {
            output.authorizeSuccessfully();
        } else {
            output.failAuthorization();
        }
    }

    private boolean checkAuthorization(String clientId, String topicName) {
        //todo
        return true;
    }
}
