package com.luche.myandroidcase.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.luche.myandroidcase.model.Lancamento
import com.luche.myandroidcase.repository.LancamentoRepository
import java.math.BigDecimal
import java.util.*

class MainActivityViewModel(
    private val repository: LancamentoRepository
) : ViewModel(){
    var balancoMes = BigDecimal.ZERO
    var listaInicializada = false;
    var spinnerIdx = Date().month
    val listaLancamentosLiveData = MutableLiveData<List<Lancamento>>()

    fun buscaLancamentos(failCallback: (msgErro: String?) -> Unit){
        if(listaLancamentosLiveData.value.isNullOrEmpty()) {
            repository.buscaLancamentos(
                { listaLancamentos ->
                    ordenaLancamentosPorMes(listaLancamentos as MutableList<Lancamento>)
                    if (listaLancamentos != null) {
                        listaInicializada = true
                        listaLancamentosLiveData.value = listaLancamentos
                    }
                },
                failCallback
            )
        }else{
            listaLancamentosLiveData.postValue(listaLancamentosLiveData.value)
        }
    }

    private fun ordenaLancamentosPorMes(listaLancamentos: MutableList<Lancamento>) {
        listaLancamentos.sortBy {
            it.mes_lancamento
        }
    }

    fun calculaBalanco(lancamentosDoMes: List<Lancamento>) : BigDecimal{
        balancoMes = BigDecimal(
            if(lancamentosDoMes.isNotEmpty()) {
                lancamentosDoMes.sumByDouble {
                    it.valor.toDouble()
                }
            }else{
                0.toDouble()
            }
        )
        //
        return balancoMes
    }

    fun atualizaIdxSpinner(idx: Int){
        if(listaInicializada) {
            spinnerIdx = idx
        }
    }


    class MainActivityViewModelFactory(
        private val repository: LancamentoRepository
    ) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainActivityViewModel(repository) as T
        }

    }

}

