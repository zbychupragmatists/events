package com.pragmatists.blog.events.application;

import com.google.common.base.Splitter;
import com.pragmatists.blog.events.EventsApplication;
import okhttp3.*;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.assertj.core.util.Lists;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.Receiver;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserResourceTest {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @LocalServerPort
    private Integer serverPort;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Rule
    public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();
    private ApiTemplate api;

    @Before
    public void setUp() {
        api = new ApiTemplate(serverPort);
        jmsTemplate.setReceiveTimeout(1_000);
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

    @Test
    public void registerPutsEmailOnQueue() throws Exception {
        JSONObject createUserJson = new JSONObject()
                .put("login", "dev-user")
                .put("email", "dev-user@pragmatists.pl");

        api.post("users", createUserJson.toString());

        String message = (String) jmsTemplate.receiveAndConvert("emails");
        List<String> emailAndToken = newArrayList(Splitter.on(";").split(message));
        assertThat(emailAndToken.get(0)).isEqualTo("dev-user@pragmatists.pl");
        assertThat(emailAndToken.get(1)).isNotEmpty();
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