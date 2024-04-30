package org.ein.erste.iot.hivemq.extension;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.auth.SimpleAuthenticator;
import com.hivemq.extension.sdk.api.auth.parameter.SimpleAuthInput;
import com.hivemq.extension.sdk.api.auth.parameter.SimpleAuthOutput;
import okhttp3.*;
import org.ein.erste.iot.hivemq.extension.util.AuthenticateRequest;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

import static org.ein.erste.iot.hivemq.extension.MyExtensionMain.CONFIG;

public class CustomAuthenticator implements SimpleAuthenticator {

    private final JsonMapper jsonMapper = new JsonMapper();

    @Override
    public void onConnect(@NotNull SimpleAuthInput simpleAuthInput, @NotNull SimpleAuthOutput simpleAuthOutput) {
        System.out.println("Trying connect: " + simpleAuthInput.getConnectPacket().getClientId());
        String clientId = new String(simpleAuthInput.getConnectPacket().getClientId());
        var passwordBytes = simpleAuthInput.getConnectPacket().getPassword();
        if (passwordBytes.isEmpty()) simpleAuthOutput.failAuthentication();

        CharBuffer charBuffer = StandardCharsets.US_ASCII.decode(passwordBytes.get());
        String password = charBuffer.toString();

        if (checkAuthentication(clientId, password)) {
            simpleAuthOutput.authenticateSuccessfully();
        } else {
            simpleAuthOutput.failAuthentication();
        }
    }

    private boolean checkAuthentication(String clientId, String password) {
        if (true) return true;
        if (CONFIG.logstashReaderId().equals(clientId) && CONFIG.logstashReaderPassword().equals(password)) {
            return true;
        }
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.get("application/json; charset=utf-8");

            var requestBody = new AuthenticateRequest(clientId, password);

            RequestBody body = RequestBody.create(jsonMapper.writeValueAsString(requestBody), JSON);
            Request request = new Request.Builder()
                    .url(CONFIG.authServerUrl() + "/api/iot/mqtt/authorize")
                    .addHeader("Hivemq-Auth", CONFIG.authServerApiKey())
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return Boolean.valueOf(response.body().string());
        } catch (Exception e){
            System.out.println("Error during authentification request: " + e);
            return false;
        }
    }
}
