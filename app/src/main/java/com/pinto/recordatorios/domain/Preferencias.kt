package com.pinto.recordatorios.domain

data class Preferencias(
    val temaOscuro: Boolean = false,
    val ordenListado: String = "fecha", // "fecha" | "prioridad" | "titulo"
    val mostrarCompletados: Boolean = true
)