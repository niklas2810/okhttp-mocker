package com.niklasarndt.okhttpmock;

import static com.niklasarndt.okhttpmock.HttpCode.HTTP_200_OK;
import static com.niklasarndt.okhttpmock.HttpCode.HTTP_401_UNAUTHORIZED;
import static com.niklasarndt.okhttpmock.MediaTypes.MEDIATYPE_JSON;
import okhttp3.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.regex.Pattern;

public class Tests {

    private static final String TEST_URL = "https://api.github.com/users/gmazzo";
    private static final String TEST_RESPONSE = "good!";
    private MockInterceptor interceptor;
    private OkHttpClient client;

    @BeforeEach
    public void setup() {
        interceptor = new MockInterceptor(MockInterceptor.Behavior.UNORDERED);
        client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    @Test
    public void testGet() throws IOException {
        interceptor.addRule()
                .get(TEST_URL)
                .respond(TEST_RESPONSE);

        client.newCall(new Request.Builder()
                .url(TEST_URL)
                .get()
                .build())
                .execute();
    }

    @Test
    public void testGetMultipleTimes() throws IOException {
        interceptor.addRule()
                .get(TEST_URL)
                .anyTimes()
                .respond(TEST_RESPONSE);

        String first = null;
        for (int i = 0; i < 10; i++) {
            String actual = client.newCall(new Request.Builder().url(TEST_URL).get().build())
                    .execute().body().string();

            if (first == null) {
                first = actual;

            } else {
                assertEquals(first, actual);
            }
        }
    }

    @Test
    public void testURLStartsWith() throws IOException {
        interceptor.addRule()
                .get()
                .urlStarts("https://")
                .respond(TEST_RESPONSE);

        client.newCall(new Request.Builder()
                .url(TEST_URL)
                .get()
                .build())
                .execute();
    }

    @Test
    public void testURLStartsWith_Fail() {
        interceptor.addRule()
                .get()
                .urlStarts("http://")
                .respond(TEST_RESPONSE)
                .code(HTTP_401_UNAUTHORIZED);

        assertThrows(AssertionError.class, () ->
                client.newCall(new Request.Builder()
                        .url(TEST_URL)
                        .get()
                        .build())
                        .execute());
    }

    @Test
    public void testResourceResponse() throws IOException {
        interceptor.addRule()
                .respond(ClasspathResources.resource("sample.json"));

        client.newCall(new Request.Builder()
                .url(TEST_URL)
                .get()
                .build())
                .execute();
    }

    @Test
    public void testCustomResponse() throws IOException {
        final String json = "{\"succeed\":true}";

        interceptor.behavior(MockInterceptor.Behavior.SEQUENTIAL).addRule()
                .get().or().post().or().put()
                .respond(json, MEDIATYPE_JSON);

        assertEquals(json, client.newCall(new Request.Builder()
                .url(TEST_URL)
                .get()
                .build())
                .execute()
                .body()
                .string());
    }

    @Test
    public void testCustomizeResponse() throws IOException {
        final ResponseBody body = ResponseBody.create("<html/>", MediaTypes.MEDIATYPE_XML);

        interceptor.addRule().respond(TEST_RESPONSE).body(body).addHeader("Test", "aValue");

        Response response = client.newCall(new Request.Builder().url(TEST_URL).get().build()).execute();

        assertEquals(MediaTypes.MEDIATYPE_XML.type(), response.body().contentType().type());
        assertEquals(MediaTypes.MEDIATYPE_XML.subtype(), response.body().contentType().subtype());
        assertEquals("aValue", response.header("Test"));
        assertEquals("<html/>", response.body().string());
    }

    @Test
    public void testWrongOrSyntax1() {
        assertThrows(IllegalStateException.class, () ->
                interceptor.addRule()
                        .or().get()
                        .respond(HttpCode.HTTP_409_CONFLICT));
    }

    @Test
    public void testWrongOrSyntax2() {
        assertThrows(IllegalStateException.class, () ->
                interceptor.addRule()
                        .get().or().or().post()
                        .respond(HttpCode.HTTP_409_CONFLICT));
    }

    @Test
    public void testWrongNotSyntax1() {
        assertThrows(IllegalStateException.class, () ->
                interceptor.addRule()
                        .put().not()
                        .respond(HttpCode.HTTP_409_CONFLICT));
    }

    @Test
    public void testWrongNotSyntax2() {
        assertThrows(IllegalStateException.class, () ->
                interceptor.addRule()
                        .not().not().put()
                        .respond(HttpCode.HTTP_409_CONFLICT));
    }

    @Test
    public void testFailReasonSequential() {
        interceptor.behavior(MockInterceptor.Behavior.SEQUENTIAL)
                .addRule()
                .get().or().post().or().put()
                .respond("OK");

        assertThrows(AssertionError.class, () ->
                client.newCall(new Request.Builder()
                        .url(TEST_URL)
                        .delete()
                        .build())
                        .execute());
    }

    @Test
    public void testAnswer() throws IOException {
        interceptor.addRule()
                .get()
                .pathMatches(Pattern.compile("/aPath/(\\w+)"))
                .anyTimes()
                .answer(request -> new Response.Builder()
                        .code(HTTP_200_OK)
                        .body(ResponseBody.create(request.url().encodedPath(), null)));

        String[] paths = new String[]{"/aPath/aaa", "/aPath/bbb", "/aPath/ccc"};
        for (String expectedBody : paths) {
            HttpUrl url = HttpUrl.parse(TEST_URL).newBuilder().encodedPath(expectedBody).build();

            String body = client.newCall(new Request.Builder()
                    .url(url)
                    .get()
                    .build())
                    .execute()
                    .body()
                    .string();

            assertEquals(expectedBody, body);
        }
    }

    @Test
    public void testPatch() throws IOException {
        final RequestBody body = RequestBody.create("{}", MediaType.parse("application/json"));
        interceptor.addRule()
                .patch(TEST_URL)
                .respond(TEST_RESPONSE);

        client.newCall(new Request.Builder()
                .url(TEST_URL)
                .patch(body)
                .build())
                .execute();
    }
}
