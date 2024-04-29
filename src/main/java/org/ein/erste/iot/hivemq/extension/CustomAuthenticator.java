package org.ein.erste.iot.hivemq.extension;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.auth.SimpleAuthenticator;
import com.hivemq.extension.sdk.api.auth.parameter.SimpleAuthInput;
import com.hivemq.extension.sdk.api.auth.parameter.SimpleAuthOutput;
import okhttp3.*;
import org.ein.erste.iot.hivemq.extension.util.AuthenticateRequest;
import org.ein.erste.iot.hivemq.extension.util.ConfigFile;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class CustomAuthenticator implements SimpleAuthenticator {

    private ConfigFile config;
    private final JsonMapper jsonMapper = new JsonMapper();
    public CustomAuthenticator(ConfigFile configFile) {
        this.config = configFile;
    }

    @Override
    public void onConnect(@NotNull SimpleAuthInput simpleAuthInput, @NotNull SimpleAuthOutput simpleAuthOutput) {
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
        if (config.logstashLogin().equals(clientId) && config.logstashPassword().equals(password)) {
            return true;
        }
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.get("application/json; charset=utf-8");

            var requestBody = new AuthenticateRequest(clientId, password);

            RequestBody body = RequestBody.create(jsonMapper.writeValueAsString(requestBody), JSON);
            Request request = new Request.Builder()
                    .url(config.url() + "/api/iot/mqtt/authorize")
                    .addHeader("Hivemq-Auth", config.apiKey())
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
