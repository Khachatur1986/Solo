package com.khach.feed.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static ApiClient apiConfig;
    private static final String BASE_URL = "https://content.guardianapis.com";
    private File cacheDir;
    private Retrofit retrofit;

    private ApiClient(File cacheDir) {
        this.cacheDir = cacheDir;
        this.retrofit = getRetroInstance();
    }


    public static ApiClient getInstance(File cacheDir) {
        if (apiConfig == null) {
            synchronized (ApiClient.class) {
                if (apiConfig == null) {
                    apiConfig = new ApiClient(cacheDir);
                }
            }
        }
        return apiConfig;
    }

    private Retrofit getRetroInstance() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            File httpCacheDirectory = new File(cacheDir, "offlineCache");

            //10 MB
            Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);

            OkHttpClient client = new OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(interceptor)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            return new Retrofit.Builder().baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }else {
            return retrofit;
        }
    }

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
