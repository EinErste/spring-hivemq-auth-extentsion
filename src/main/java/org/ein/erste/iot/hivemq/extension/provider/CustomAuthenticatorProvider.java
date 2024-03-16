package org.ein.erste.iot.hivemq.extension.provider;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.annotations.Nullable;
import com.hivemq.extension.sdk.api.auth.Authenticator;
import com.hivemq.extension.sdk.api.auth.parameter.AuthenticatorProviderInput;
import com.hivemq.extension.sdk.api.services.auth.provider.AuthenticatorProvider;
import org.ein.erste.iot.hivemq.extension.CustomAuthenticator;
import org.ein.erste.iot.hivemq.extension.util.ConfigFile;

public class CustomAuthenticatorProvider implements AuthenticatorProvider {
    private ConfigFile config;
    public CustomAuthenticatorProvider(ConfigFile config) {
        this.config = config;
    }

    @Override
    public @Nullable Authenticator getAuthenticator(@NotNull AuthenticatorProviderInput authenticatorProviderInput) {
        return new CustomAuthenticator(config);
    }
}
