package com.binarChallenge.mymovies.data

import com.binarChallenge.mymovies.GetMoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = "acb0afb6699c9aa6945f1d6a3f1ec89c",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String = "acb0afb6699c9aa6945f1d6a3f1ec89c",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String = "acb0afb6699c9aa6945f1d6a3f1ec89c",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>
}