package org.ein.erste.iot.hivemq.extension;

import com.hivemq.extension.sdk.api.ExtensionMain;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.parameter.ExtensionStartInput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStartOutput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStopInput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStopOutput;
import com.hivemq.extension.sdk.api.services.Services;
import org.ein.erste.iot.hivemq.extension.provider.CustomAuthenticatorProvider;
import org.ein.erste.iot.hivemq.extension.provider.CustomPublishAuthorizerProvider;

public class MyExtensionMain implements ExtensionMain {
    @Override
    public void extensionStart(@NotNull ExtensionStartInput extensionStartInput, @NotNull ExtensionStartOutput extensionStartOutput) {
        System.out.println("Custom auth extension started");
        Services.securityRegistry().setAuthenticatorProvider(new CustomAuthenticatorProvider());
        Services.securityRegistry().setAuthorizerProvider(new CustomPublishAuthorizerProvider());
    }

    @Override
    public void extensionStop(@NotNull ExtensionStopInput extensionStopInput, @NotNull ExtensionStopOutput extensionStopOutput) {

    }
}
