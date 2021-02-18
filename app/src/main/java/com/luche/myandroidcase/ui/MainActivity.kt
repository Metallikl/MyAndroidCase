package com.luche.myandroidcase.ui

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
        LancamentoAdapter(
            this,
            listaLancamentos
        ) { position, lancamento ->
            trataCliqueLancamento(position,lancamento)
        }
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

    private fun trataCliqueLancamento(position: Int, lancamento: Lancamento) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog
            .setTitle("Detalhes do Lancamento")
            .setMessage("Deseja ver os detalhes lançamento ?")
            .setNegativeButton("Não",null)
            .setPositiveButton("Sim")
            { dialogInterface: DialogInterface, i: Int ->
                chamaActLancamentoDetalhes(lancamento)
            }
        alertDialog.create().show()

    }

    private fun chamaActLancamentoDetalhes(lancamento: Lancamento) {
        val intent = Intent(this,LancamentoDetalhes::class.java)
        val bundle = Bundle()
        bundle.putSerializable(LANCAMENTO_SAFE_PARAM,lancamento)
        intent.putExtras(bundle)
        startActivity(intent)
        //Não matarei a tela incial, pois se não geraria uma nova chamada a API ou seria necessario
        //persistir ou trafegar a lista via bundle.
    }


    companion object {
        const val LANCAMENTO_SAFE_PARAM = "BUNDLE_PARAM_LANCAMENTO"
    }
}