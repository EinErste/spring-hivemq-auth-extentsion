package org.ein.erste.iot.hivemq.extension.provider;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.annotations.Nullable;
import com.hivemq.extension.sdk.api.auth.Authenticator;
import com.hivemq.extension.sdk.api.auth.Authorizer;
import com.hivemq.extension.sdk.api.auth.parameter.AuthenticatorProviderInput;
import com.hivemq.extension.sdk.api.auth.parameter.AuthorizerProviderInput;
import com.hivemq.extension.sdk.api.services.auth.provider.AuthenticatorProvider;
import com.hivemq.extension.sdk.api.services.auth.provider.AuthorizerProvider;
import org.ein.erste.iot.hivemq.extension.CustomAuthenticator;
import org.ein.erste.iot.hivemq.extension.CustomPublishAuthorizer;

public class CustomPublishAuthorizerProvider implements AuthorizerProvider {
    @Override
    public @Nullable Authorizer getAuthorizer(@NotNull AuthorizerProviderInput authorizerProviderInput) {
        return new CustomPublishAuthorizer();
    }
}
