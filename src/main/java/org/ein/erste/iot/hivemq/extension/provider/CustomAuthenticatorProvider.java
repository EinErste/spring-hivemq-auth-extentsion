package org.ein.erste.iot.hivemq.extension.provider;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.annotations.Nullable;
import com.hivemq.extension.sdk.api.auth.Authenticator;
import com.hivemq.extension.sdk.api.auth.parameter.AuthenticatorProviderInput;
import com.hivemq.extension.sdk.api.services.auth.provider.AuthenticatorProvider;
import org.ein.erste.iot.hivemq.extension.CustomAuthenticator;

public class CustomAuthenticatorProvider implements AuthenticatorProvider {
    @Override
    public @Nullable Authenticator getAuthenticator(@NotNull AuthenticatorProviderInput authenticatorProviderInput) {
        return new CustomAuthenticator();
    }
}
