package com.luche.myandroidcase.repository

import com.luche.myandroidcase.model.Categoria
import com.luche.myandroidcase.retrofit.webclient.CategoriaClient

class CategoriaRepository(
        private val client: CategoriaClient = CategoriaClient()
) {
        fun buscarCategorias(sucessCallback: (listaCategorias: List<Categoria>?) -> Unit,
                             failCallback: (msgErro: String?) -> Unit){
            client.buscaCategorias(
                    sucessCallback,
                    failCallback
            )
        }
}