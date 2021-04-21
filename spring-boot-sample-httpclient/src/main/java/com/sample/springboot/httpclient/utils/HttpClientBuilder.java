package com.sample.springboot.httpclient.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Map;

@Slf4j
public class HttpClientBuilder {

    private static final String HTTP_SCHEME = "http";

    private static final String HTTPS_SCHEME = "https";

    private URIBuilder uriBuilder;

    private HttpEntity httpEntity;

    public HttpClientBuilder(URIBuilder uriBuilder) {
        this.uriBuilder = uriBuilder;
    }

    public static HttpClientBuilder builder(String host, String path) {
        URIBuilder uriBuilder = new URIBuilder()
                .setHost(host)
                .setPath(path);
        return new HttpClientBuilder(uriBuilder);
    }

    public HttpClientBuilder http() {
        this.uriBuilder.setScheme(HTTP_SCHEME);
        return this;
    }

    public HttpClientBuilder https() {
        this.uriBuilder.setScheme(HTTPS_SCHEME);
        return this;
    }

    public String get() {
        return get(null);
    }

    public String get(Map<String, String> parameters) {
        // 设置请求参数
        // https://hc.apache.org/httpcomponents-client-4.5.x/tutorial/html/fundamentals.html#1.1.1
        if (!CollectionUtils.isEmpty(parameters)) {
            parameters.forEach(uriBuilder::setParameter);
        }

        String responseBody = "";
        try {
            HttpGet httpget = new HttpGet(uriBuilder.build());
            responseBody = execute(httpget);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    public String post(String json) {
        String responseBody = "";
        try {
            HttpPost httppost = new HttpPost(uriBuilder.build());
            StringEntity entity = new StringEntity(json);
            httppost.setEntity(entity);
            responseBody = execute(httppost);
        } catch (URISyntaxException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    public String post(Map<String, Object> parameters) {
        String responseBody = "";
        try {
            HttpPost httppost = new HttpPost(uriBuilder.build());
            HttpEntity entity = createEntity(parameters);
            httppost.setEntity(entity);
            responseBody = execute(httppost);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    public String put(String json) {
        String responseBody = "";
        try {
            HttpPut httpput = new HttpPut(uriBuilder.build());
            StringEntity entity = new StringEntity(json);
            httpput.setEntity(entity);
            responseBody = execute(httpput);
        } catch (URISyntaxException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    public String put(Map<String, Object> parameters) {
        String responseBody = "";
        try {
            HttpPut httpput = new HttpPut(uriBuilder.build());
            HttpEntity entity = createEntity(parameters);
            httpput.setEntity(entity);
            responseBody = execute(httpput);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    public String delete() {
        String responseBody = "";
        try {
            HttpDelete httpdelete = new HttpDelete(uriBuilder.build());
            responseBody = execute(httpdelete);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    /**
     * 生成请求体
     *
     * https://hc.apache.org/httpcomponents-client-4.5.x/httpmime/examples/org/apache/http/examples/entity/mime/ClientMultipartFormPost.java
     */
    private HttpEntity createEntity(Map<String, Object> parameters) {
        if (CollectionUtils.isEmpty(parameters)) {
            return MultipartEntityBuilder.create().build();
        }

        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        parameters.forEach((key, value) -> {
            if (value instanceof String) {
                StringBody stringBody = new StringBody((String) value, ContentType.TEXT_PLAIN);
                entityBuilder.addPart(key, stringBody);
            } else if (value instanceof File) {
                FileBody fileBody = new FileBody((File) value);
                entityBuilder.addPart(key, fileBody);
            }
        });
        return entityBuilder.build();
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

}
