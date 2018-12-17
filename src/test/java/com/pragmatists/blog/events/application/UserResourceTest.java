package com.pragmatists.blog.events.application;

import com.google.common.base.Splitter;
import com.pragmatists.blog.events.EventsApplication;
import okhttp3.*;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract public class UserResourceTest {

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
        assertThat(jsonObject.getString("id")).isNotNull();
    }

@Test
public void registerPutsEmailAndTokenOnQueue() throws Exception {
    JSONObject createUserJson = new JSONObject()
            .put("login", "dev-user2")
            .put("email", "dev-user2@pragmatists.pl");

    api.post("users", createUserJson.toString());

    String message = (String) jmsTemplate.receiveAndConvert("emails");
    List<String> emailAndToken = newArrayList(Splitter.on(";").split(message));
    assertThat(emailAndToken.get(0)).isEqualTo("dev-user2@pragmatists.pl");
    assertThat(emailAndToken.get(1)).isNotEmpty();
}

    @Test
    // @Ignore // Unignore this to see if transaction inside listener was committed - should fail without
    // @Transactional(propagation = Propagation.REQUIRES_NEW) on listener method
    public void registeredUserHasTokenGenerated() throws Exception {
        String userId = givenUserCreated("dev-user3", "dev-user3@pragmatists.pl");

        String response = api.get(format("users/%s", userId));

        JSONObject jsonObject = new JSONObject(response);
        assertThat(jsonObject.getString("emailToken")).isNotNull();
    }

    private String givenUserCreated(String login, String email) throws JSONException, IOException {
        JSONObject createUserJson = new JSONObject()
                .put("login", login)
                .put("email", email);
        String response = api.post("users", createUserJson.toString());
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getString("id");
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
            return format("http://localhost:%s/%s", this.serverPort, url);
        }

        public String get(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(withBaseUrl(url))
                    .get()
                    .build();
            Response response = this.client.newCall(request).execute();
            return response.body().string();
        }
    }
}