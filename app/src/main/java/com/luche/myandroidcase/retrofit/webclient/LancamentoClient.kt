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

    fun buscaLancamentos(
        sucessCallback: (listaLancamentos: List<Lancamento>?) -> Unit,
        failCallback : (msgErro: String?) -> Unit
    ){
        MyAndroidCaseRetrofit().executaChamada(
            service.getLancamentos(),
            sucessCallback,
            failCallback
        )
    }
}