package com.luche.myandroidcase.retrofit

import com.luche.myandroidcase.retrofit.service.LancamentoService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://desafio-it-server.herokuapp.com/"

class MyAndroidCaseRetrofit {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val lancamentoService : LancamentoService by lazy{
        retrofit.create(LancamentoService::class.java)
    }
}