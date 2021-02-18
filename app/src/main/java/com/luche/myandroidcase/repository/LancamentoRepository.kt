package com.luche.myandroidcase.repository

import com.luche.myandroidcase.model.Lancamento
import com.luche.myandroidcase.retrofit.webclient.LancamentoClient

class LancamentoRepository(
    private val client: LancamentoClient  = LancamentoClient()
) {

    fun buscaLancamentos(sucessCallback: (listaLancamentos: List<Lancamento>?) -> Unit, failCallback : (msgErro: String?) -> Unit ){
        client.buscaLancamentos(
            sucessCallback,
            failCallback
        )
    }
}