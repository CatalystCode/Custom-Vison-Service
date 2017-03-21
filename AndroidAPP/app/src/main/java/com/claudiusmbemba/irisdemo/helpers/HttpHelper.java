package com.claudiusmbemba.irisdemo.helpers;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class HttpHelper {

    public static String makeRequest(RequestPackage requestPackage, InputStream data)
            throws Exception {

        String address = requestPackage.getEndpoint();

        OkHttpClient client = new OkHttpClient();

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.addHeader("Prediction-Key","1f63500d4fab43a095da927acd22aa60");

        if (requestPackage.getParams().containsKey("Url")){
            requestBuilder.addHeader("Content-Type","application/json");
        } else {
            requestBuilder.addHeader("Content-Type","application/octet-stream");
        }

        if (requestPackage.getMethod().equals("POST")) {
            RequestBody requestBody = null;
            if (requestPackage.getParams().containsKey("Url")) {
                JSONObject json = new JSONObject(requestPackage.getParams());
                requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
            } else {
                if(data != null) {
                    requestBody = RequestBodyUtil.create(MediaType.parse("application/octet-stream; charset=utf-8"), data);
                } else {
                    throw new Exception("No image data found");
                }
            }
            requestBuilder.method("POST", requestBody);

        } else if (requestPackage.getMethod().equals("GET")) {
            address = String.format("%s?%s", address, requestPackage.getEncodedParams());
        }

        requestBuilder.url(address);

        Request request = requestBuilder.build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Exception: response code " + response.code());
        }
    }
}

class RequestBodyUtil {

    public static RequestBody create(final MediaType mediaType, final InputStream inputStream) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public long contentLength() {
                try {
                    return inputStream.available();
                } catch (IOException e) {
                    return 0;
                }
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(inputStream);
                    sink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }
}
