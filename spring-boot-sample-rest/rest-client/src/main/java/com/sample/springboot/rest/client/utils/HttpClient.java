package com.sample.springboot.rest.client.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpClient {

    private static final String DEFAULT_SCHEME = "http";

    private static final String DEFAULT_HOST = "localhost";

    private static final String DEFAULT_PATH = "/";

    private static final int DEFAULT_PORT = 8080;

    private URI uri;

    public HttpClient(URI uri) {
        this.uri = uri;
    }

    public String get() {
        HttpUriRequest request = RequestBuilder
                .get(uri)
                .build();

        return execute(request);
    }

    public String post(String text) {
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException();
        }

        HttpUriRequest request = RequestBuilder
                .put(uri)
                .setEntity(createTextEntity(text))
                .build();

        return execute(request);
    }

    public String post(Map<String, String> parameters) {
        if (CollectionUtils.isEmpty(parameters)) {
            throw new IllegalArgumentException();
        }

        HttpUriRequest request = RequestBuilder
                .put(uri)
                .setEntity(createFormParamsEntity(parameters))
                .build();

        return execute(request);
    }

    public String put(String text) {
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException();
        }

        HttpUriRequest request = RequestBuilder
                .put(uri)
                .setEntity(createTextEntity(text))
                .build();

        return execute(request);
    }

    public String put(Map<String, String> parameters) {
        if (CollectionUtils.isEmpty(parameters)) {
            throw new IllegalArgumentException();
        }

        HttpUriRequest request = RequestBuilder
                .put(uri)
                .setEntity(createFormParamsEntity(parameters))
                .build();

        return execute(request);
    }

    public String delete() {
        HttpUriRequest request = RequestBuilder
                .delete(uri)
                .build();

        return execute(request);
    }

    /**
     * 生成Text(Json)类型entity
     */
    private HttpEntity createTextEntity(String text) {
        return EntityBuilder
                .create()
                .setText(text)
                .setContentType(ContentType.APPLICATION_JSON)
                .build();
    }

    /**
     * 生成Form类型entity
     */
    private HttpEntity createFormParamsEntity(Map<String, String> parameters) {
        List<NameValuePair> formparams = new ArrayList<>();
        parameters.forEach((key, value) -> formparams.add(new BasicNameValuePair(key, value)));

        return EntityBuilder
                .create()
                .setParameters(formparams)
                .setContentType(ContentType.TEXT_PLAIN)
                .build();
    }

    /**
     * 响应处理
     *
     * https://hc.apache.org/httpcomponents-client-4.5.x/httpclient/examples/org/apache/http/examples/client/ClientWithResponseHandler.java
     */
    private String execute(HttpUriRequest request) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String responseBody = "";

        try {
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            responseBody = httpclient.execute(request, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return responseBody;
    }

    public static HttpClientBuilder builder() {
        URIBuilder uriBuilder = new URIBuilder()
                .setScheme(DEFAULT_SCHEME)
                .setHost(DEFAULT_HOST)
                .setPath(DEFAULT_PATH)
                .setPort(DEFAULT_PORT);
        return new HttpClientBuilder(uriBuilder);
    }

    @Getter
    public static class HttpClientBuilder {

        private URIBuilder uriBuilder;

        HttpClientBuilder(URIBuilder uriBuilder) {
            this.uriBuilder = uriBuilder;
        }

        public HttpClientBuilder https() {
            this.uriBuilder.setScheme("https");
            return this;
        }

        public HttpClientBuilder host(String host) {
            this.uriBuilder.setHost(host);
            return this;
        }

        public HttpClientBuilder path(String path) {
            this.uriBuilder.setPath(path);
            return this;
        }

        public HttpClientBuilder port(int port) {
            this.uriBuilder.setPort(port);
            return this;
        }

        public HttpClientBuilder parameter(String param, String value) {
            this.uriBuilder.setParameter(param, value);
            return this;
        }

        public HttpClient build() {
            URI uri = null;
            try {
                uri = uriBuilder.build();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return new HttpClient(uri);
        }
    }

}
