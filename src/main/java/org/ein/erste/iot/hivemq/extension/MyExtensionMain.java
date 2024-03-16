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
import org.ein.erste.iot.hivemq.extension.util.ConfigFile;

import java.io.File;

public class MyExtensionMain implements ExtensionMain {
    @Override
    public void extensionStart(@NotNull ExtensionStartInput extensionStartInput, @NotNull ExtensionStartOutput extensionStartOutput) {
        System.out.println("Custom auth extension started");
        File configFile = new File(extensionStartInput.getExtensionInformation().getExtensionHomeFolder(), "auth-config.ini");
        String[] authConfigStr = configFile.list();
        ConfigFile config = new ConfigFile(authConfigStr[0], authConfigStr[1]);

        Services.securityRegistry().setAuthenticatorProvider(new CustomAuthenticatorProvider(config));
        Services.securityRegistry().setAuthorizerProvider(new CustomPublishAuthorizerProvider(config));
    }

    @Override
    public void extensionStop(@NotNull ExtensionStopInput extensionStopInput, @NotNull ExtensionStopOutput extensionStopOutput) {

    }
}
