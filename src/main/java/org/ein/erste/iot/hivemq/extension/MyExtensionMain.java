package org.ein.erste.iot.hivemq.extension;

import com.hivemq.extension.sdk.api.ExtensionMain;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.parameter.ExtensionStartInput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStartOutput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStopInput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStopOutput;
import com.hivemq.extension.sdk.api.services.Services;
import com.hivemq.extension.sdk.api.services.intializer.ClientInitializer;
import com.hivemq.extension.sdk.api.services.intializer.InitializerRegistry;
import org.apache.commons.io.FileUtils;
import org.ein.erste.iot.hivemq.extension.provider.CustomAuthenticatorProvider;
import org.ein.erste.iot.hivemq.extension.provider.CustomPublishAuthorizerProvider;
import org.ein.erste.iot.hivemq.extension.util.ConfigFile;

import java.io.File;
import java.util.List;

public class MyExtensionMain implements ExtensionMain {
    @Override
    public void extensionStart(@NotNull ExtensionStartInput extensionStartInput, @NotNull ExtensionStartOutput extensionStartOutput) {
        System.out.println("Custom auth extension started");
        List<String> authConfigStr = null;
        try {
            authConfigStr = FileUtils.readLines(
                    new File(extensionStartInput.getExtensionInformation().getExtensionHomeFolder(), "auth-config.ini"), "utf-8");

        } catch (Exception e) {
            throw new RuntimeException("Failed to read config file", e);
        }
        ConfigFile config = new ConfigFile(authConfigStr.get(0), authConfigStr.get(1), authConfigStr.get(2), authConfigStr.get(3));

        Services.securityRegistry().setAuthenticatorProvider(new CustomAuthenticatorProvider(config));
        Services.securityRegistry().setAuthorizerProvider(new CustomPublishAuthorizerProvider(config));
        Services.initializerRegistry().setClientInitializer((initializerInput, clientContext) -> {
            clientContext.addPublishInboundInterceptor(new PublishInterceptor());
        });
    }

    @Override
    public void extensionStop(@NotNull ExtensionStopInput extensionStopInput, @NotNull ExtensionStopOutput extensionStopOutput) {

    }
}
