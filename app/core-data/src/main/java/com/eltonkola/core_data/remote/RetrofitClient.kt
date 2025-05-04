package com.eltonkola.core_data.remote

import com.eltonkola.core_data.remote.api.PolygonService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://api.polygon.io/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            //TODO- fix this later, by injection
//            if(BuildConfig.DEBUG) {
//                level = HttpLoggingInterceptor.Level.BODY
//            }
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val polygonService: PolygonService = retrofit.create(PolygonService::class.java)
}
