package com.luche.myandroidcase.retrofit

import com.luche.myandroidcase.retrofit.service.CategoriaService
import com.luche.myandroidcase.retrofit.service.LancamentoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    val categoriaService : CategoriaService by lazy{
        retrofit.create(CategoriaService::class.java)
    }

    fun <T> executaChamada(
            call: Call<T>,
            sucessCallback: (dados: T?) -> Unit,
            failCallback : (msgErro: String?) -> Unit
    ) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if(response.isSuccessful){
                    sucessCallback(response.body())
                }else{
                    failCallback(response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                failCallback(t.message)
            }
        })
    }
}