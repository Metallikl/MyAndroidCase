package com.luche.myandroidcase.retrofit.webclient

import com.luche.myandroidcase.model.Categoria
import com.luche.myandroidcase.retrofit.MyAndroidCaseRetrofit
import com.luche.myandroidcase.retrofit.service.CategoriaService

class CategoriaClient(
        private val service: CategoriaService = MyAndroidCaseRetrofit().categoriaService
) {

    fun buscaCategorias(sucessCallback: (listaCategorias: List<Categoria>?) -> Unit,
                        failCallback: (msgErro: String?) -> Unit)
    {
        MyAndroidCaseRetrofit().executaChamada(
                service.getCategorias(),
                sucessCallback,
                failCallback
        )
    }

}