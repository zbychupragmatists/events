package com.pragmatists.blog.events.application;

import com.pragmatists.blog.events.EventsApplication;
import okhttp3.*;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserResourceTest {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @LocalServerPort
    private Integer serverPort;

    private ApiTemplate api;

    @Before
    public void setUp() {
        api = new ApiTemplate(serverPort);
    }

    @Test
    public void registerUser() throws Exception {

        JSONObject createUserJson = new JSONObject()
                .put("login", "dev-user")
                .put("email", "dev-user@pragmatists.pl");

        String response = api.post("users", createUserJson.toString());

        JSONObject jsonObject = new JSONObject(response);
        assertThat(jsonObject.get("id")).isNotNull();
    }

    private class ApiTemplate {

        private final OkHttpClient client;
        private final Integer serverPort;

        public ApiTemplate(Integer serverPort) {
            this.serverPort = serverPort;
            this.client = new OkHttpClient();
        }

        String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(withBaseUrl(url))
                    .post(body)
                    .build();
            Response response = this.client.newCall(request).execute();
            return response.body().string();
        }

        private String withBaseUrl(String url) {
            return String.format("http://localhost:%s/%s", this.serverPort, url);
        }
    }
}