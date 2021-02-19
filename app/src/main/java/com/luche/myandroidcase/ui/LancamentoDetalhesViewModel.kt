package com.luche.myandroidcase.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luche.myandroidcase.model.Categoria
import com.luche.myandroidcase.repository.CategoriaRepository

class LancamentoDetalhesViewModel(private val repository: CategoriaRepository): ViewModel() {

    val listaCategoriaLiveData = MutableLiveData<List<Categoria>>()

    fun buscaCategorias(failCallback: (msgErro: String?) -> Unit){
        repository.buscarCategorias(
                {   listaCategorias ->
                    listaCategoriaLiveData.value = listaCategorias
                }
                ,failCallback
        )
    }

    class LancamentoDetalhesViewModelFactory(
            private val repository: CategoriaRepository
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return  LancamentoDetalhesViewModel(repository) as T
        }
    }
}
