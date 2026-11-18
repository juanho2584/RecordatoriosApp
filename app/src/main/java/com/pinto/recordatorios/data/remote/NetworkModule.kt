package com.pinto.recordatorios.data.remote

import com.pinto.recordatorios.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {

    // Interceptor para ver en el Logcat las peticiones y respuestas
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(10, TimeUnit.SECONDS) // Timeout de conexión
        .readTimeout(10, TimeUnit.SECONDS)    // Timeout de lectura
        .build()

    // Instancia única de Retrofit
    val api: RecordatorioApi by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecordatorioApi::class.java)
    }
}