package org.ein.erste.iot.hivemq.extension;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.auth.PublishAuthorizer;
import com.hivemq.extension.sdk.api.auth.parameter.PublishAuthorizerInput;
import com.hivemq.extension.sdk.api.auth.parameter.PublishAuthorizerOutput;
import com.hivemq.extension.sdk.api.packets.publish.PublishPacket;
import okhttp3.*;
import org.ein.erste.iot.hivemq.extension.util.AuthenticateRequest;
import org.ein.erste.iot.hivemq.extension.util.ConfigFile;

public class CustomPublishAuthorizer implements PublishAuthorizer {
    private ConfigFile config;
    private final JsonMapper jsonMapper = new JsonMapper();

    public CustomPublishAuthorizer(ConfigFile config) {
        this.config = config;
    }

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
        if (config.logstashLogin().equals(clientId) && topicName.equals(ConfigFile.LOGSTASH_TOPIC)) {
            return true;
        }
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.get("application/json; charset=utf-8");

            var requestBody = new AuthenticateRequest(clientId, topicName);

            RequestBody body = RequestBody.create(jsonMapper.writeValueAsString(requestBody), JSON);
            Request request = new Request.Builder()
                    .url(config.url() + "/api/iot/mqtt/authenticate")
                    .addHeader("Hivemq-Auth", config.apiKey())
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return Boolean.valueOf(response.body().string());
        } catch (Exception e){
            System.out.println("Error during authorization request: " + e);
            return false;
        }
    }
}
