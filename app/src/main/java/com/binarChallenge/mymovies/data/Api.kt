package com.binarChallenge.mymovies.data

import com.binarChallenge.mymovies.GetMoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = "c9c5ea53799624204822e99e30c87b54",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String = "c9c5ea53799624204822e99e30c87b54",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String = "c9c5ea53799624204822e99e30c87b54",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>
}