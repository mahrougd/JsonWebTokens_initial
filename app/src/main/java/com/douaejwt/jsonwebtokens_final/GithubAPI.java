package com.douaejwt.jsonwebtokens_final;

import java.util.List;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by douae on 12/11/17.
 */

public interface GithubAPI {
    public static final String ENDPOINT = "https://api.github.com";

    @Headers("Cache-Control: max-age=640000")
    @GET("/users/{user}/repos")
    List<Depot> listRepos(@Path("user") String user);

    @GET("/users/{user}/repos")
    void listReposAsync(@Path("user") String user, Callback<List<Depot>> callback);

    @GET("/search/repositories")
    List<Depot> searchRepos(@Query("q") String query);
}
