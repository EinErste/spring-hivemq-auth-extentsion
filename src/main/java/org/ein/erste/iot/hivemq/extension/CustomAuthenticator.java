package org.ein.erste.iot.hivemq.extension;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.auth.SimpleAuthenticator;
import com.hivemq.extension.sdk.api.auth.parameter.SimpleAuthInput;
import com.hivemq.extension.sdk.api.auth.parameter.SimpleAuthOutput;

import java.nio.ByteBuffer;

public class CustomAuthenticator implements SimpleAuthenticator {
    @Override
    public void onConnect(@NotNull SimpleAuthInput simpleAuthInput, @NotNull SimpleAuthOutput simpleAuthOutput) {
        String clientId = new String(simpleAuthInput.getConnectPacket().getClientId());
        String password = new String(simpleAuthInput.getConnectPacket().getPassword().orElse(ByteBuffer.allocate(1)).asCharBuffer().array());
        if (checkAuthentification(clientId, password)) {
            simpleAuthOutput.authenticateSuccessfully();
        } else {
            simpleAuthOutput.failAuthentication();
        }
    }

    private boolean checkAuthentification(String clientId, String topicName) {
        //todo
        return true;
    }
}
