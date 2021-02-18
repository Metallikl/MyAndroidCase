package com.luche.myandroidcase.retrofit.webclient

import com.luche.myandroidcase.model.Lancamento
import com.luche.myandroidcase.retrofit.MyAndroidCaseRetrofit
import com.luche.myandroidcase.retrofit.service.LancamentoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LancamentoClient(
    private val service: LancamentoService = MyAndroidCaseRetrofit().lancamentoService
) {

    private fun executaChamada(
        call: Call<List<Lancamento>>,
        sucessCallback: (listaLancamentos: List<Lancamento>?) -> Unit,
        failCallback : (msgErro: String?) -> Unit
    ){
        call.enqueue(object : Callback<List<Lancamento>>{
            override fun onResponse(
                call: Call<List<Lancamento>>,
                response: Response<List<Lancamento>>
            ) {
                if(response.isSuccessful){
                    sucessCallback(response.body())
                }else{
                    failCallback(response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Lancamento>>, t: Throwable) {
                failCallback(t.message)
            }

        })
    }

    fun buscaLancamentos(
        sucessCallback: (listaLancamentos: List<Lancamento>?) -> Unit,
        failCallback : (msgErro: String?) -> Unit
    ){
        executaChamada(
            service.getLancamentos(),
            sucessCallback,
            failCallback
        )
    }
}