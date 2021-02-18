package com.luche.myandroidcase.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.luche.myandroidcase.R
import com.luche.myandroidcase.adapter.LancamentoAdapter
import com.luche.myandroidcase.databinding.ActivityMainBinding
import com.luche.myandroidcase.model.Lancamento
import com.luche.myandroidcase.repository.LancamentoRepository

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var listaLancamentos = mutableListOf<Lancamento>()
    private val lancamentoAdapter: LancamentoAdapter by lazy{
        LancamentoAdapter(this, listaLancamentos)
    }
    private val lancamentoRepository: LancamentoRepository by lazy{
        LancamentoRepository()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setaBuscaUI(true)
        buscaLancamentos()
        iniRecycle()
    }

    private fun setaBuscaUI(buscaAtiva: Boolean) {
        if(buscaAtiva){
            binding.mainActTvLbl.text = getString(R.string.main_act_buscando_lancamentos_msg)
            binding.mainActPbLoading.visibility =  View.VISIBLE
        }else{
            binding.mainActTvLbl.text = getString(R.string.main_act_lancamentos_encontrados_msg)
            binding.mainActPbLoading.visibility =  View.GONE
        }
    }

    private fun buscaLancamentos() {
        lancamentoRepository.buscaLancamentos(
            {
                it?.let {
                    listaLancamentos = it as MutableList<Lancamento>
                    /*listaLancamentos.sortBy { lancamento ->
                        lancamento.mes_lancamento
                    }*/
                    lancamentoAdapter.atualizaLista(listaLancamentos)
                }
                setaBuscaUI(false)
            },
            {
                Toast.makeText(
                    this,
                    it,
                    Toast.LENGTH_SHORT
                ).show()
                setaBuscaUI(false)
            }
        )
    }

    private fun iniRecycle() {
        val divisor = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.mainActRvLancamentos.addItemDecoration(divisor)
        binding.mainActRvLancamentos.adapter = lancamentoAdapter
        binding.mainActRvLancamentos.layoutManager = LinearLayoutManager(this)
    }
}