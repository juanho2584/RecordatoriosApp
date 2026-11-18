package com.pinto.recordatorios.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RecordatorioApi {

    @GET("posts")
    suspend fun obtenerRecordatorios(): List<RecordatorioDto>

    @POST("posts")
    suspend fun crearRecordatorio(@Body dto: RecordatorioDto): RecordatorioDto
}