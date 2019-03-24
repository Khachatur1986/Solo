package com.khach.feed.client;

import com.khach.feed.pojo.Feed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IFeedService {
    @GET("search")
    Call<Feed> loadFeeds(@Query("q") String q,
                         @Query("format") String format,
                         @Query("show-fields") String showFields,
                         @Query("show-elements") String showElements,
                         @Query("api-key") String apiKey);


    @GET("search")
    Call<Feed> loadFeeds(@Query("q") String q,
                         @Query("format") String format,
                         @Query("show-fields") String showFields,
                         @Query("show-elements") String showElements,
                         @Query("page") Integer page,
                         @Query("api-key") String apiKey);
}
