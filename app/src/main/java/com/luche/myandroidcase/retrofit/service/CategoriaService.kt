package com.luche.myandroidcase.retrofit.service

import com.luche.myandroidcase.model.Categoria
import retrofit2.Call
import retrofit2.http.GET

interface CategoriaService {

    @GET("categorias")
    fun getCategorias() : Call<List<Categoria>>
}