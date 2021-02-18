package com.luche.myandroidcase.retrofit.service

import com.luche.myandroidcase.model.Lancamento
import retrofit2.Call
import retrofit2.http.GET

interface LancamentoService {

    @GET("lancamentos")
    fun getLancamentos() : Call<List<Lancamento>>

}